package com.example.stockmarket.controller;

import com.example.stockmarket.dto.AdminOrderRequest;
import com.example.stockmarket.entity.AdminOrder;
import com.example.stockmarket.entity.Holding;
import com.example.stockmarket.entity.PriceUpdate;
import com.example.stockmarket.entity.Stock;
import com.example.stockmarket.entity.User;
import com.example.stockmarket.repository.AdminOrderRepository;
import com.example.stockmarket.repository.HoldingRepository;
import com.example.stockmarket.repository.PriceUpdateRepository;
import com.example.stockmarket.repository.StockRepository;
import com.example.stockmarket.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "*")
public class AdminOrderController {

    private final AdminOrderRepository adminOrderRepository;
    private final PriceUpdateRepository priceUpdateRepository;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final HoldingRepository holdingRepository;
    private final com.example.stockmarket.service.MarketBrainService marketBrainService;

    public AdminOrderController(AdminOrderRepository adminOrderRepository,
                                PriceUpdateRepository priceUpdateRepository,
                                StockRepository stockRepository,
                                UserRepository userRepository,
                                HoldingRepository holdingRepository,
                                com.example.stockmarket.service.MarketBrainService marketBrainService) {
        this.adminOrderRepository = adminOrderRepository;
        this.priceUpdateRepository = priceUpdateRepository;
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
        this.holdingRepository = holdingRepository;
        this.marketBrainService = marketBrainService;
    }

    @GetMapping
    public List<Map<String, Object>> getAllOrders() {
        return adminOrderRepository.findAll()
                .stream()
                .filter(o -> "PENDING".equals(o.getStatus()))
                .sorted(Comparator.comparing(AdminOrder::getId).reversed())
                .map(this::mapOrderResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("/add")
    public Map<String, Object> addOrder(@RequestBody AdminOrderRequest request) {
        Map<String, Object> response = new HashMap<>();

        // Team fields are now optional and default to 'Direct'
        String buyerTeam = (request.getBuyerTeam() == null || request.getBuyerTeam().trim().isEmpty()) ? "Direct" : request.getBuyerTeam().trim();
        String sellerTeam = (request.getSellerTeam() == null || request.getSellerTeam().trim().isEmpty()) ? "Direct" : request.getSellerTeam().trim();


        if (request.getBuyerUsername() == null || request.getBuyerUsername().trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Buyer username is required");
            return response;
        }

        if (request.getSellerUsername() == null || request.getSellerUsername().trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Seller username is required");
            return response;
        }

        if (request.getStockName() == null || request.getStockName().trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Stock name is required");
            return response;
        }

        if (request.getPrice() <= 0) {
            response.put("success", false);
            response.put("message", "Price must be greater than 0");
            return response;
        }

        if (request.getQuantity() <= 0) {
            response.put("success", false);
            response.put("message", "Quantity must be greater than 0");
            return response;
        }

        double orderValue = request.getPrice() * request.getQuantity();
        if (orderValue > 50000) {
            response.put("success", false);
            response.put("message", "Order value cannot exceed 50000");
            return response;
        }

        String buyerUsername = request.getBuyerUsername().trim();
        String sellerUsername = request.getSellerUsername().trim();

        if (buyerUsername.equalsIgnoreCase(sellerUsername)) {
            response.put("success", false);
            response.put("message", "Buyer and seller cannot be same");
            return response;
        }

        Optional<User> buyerOptional = userRepository.findByUsername(buyerUsername);
        Optional<User> sellerOptional = userRepository.findByUsername(sellerUsername);

        if (buyerOptional.isEmpty()) {
            response.put("success", false);
            response.put("message", "Buyer username not found");
            return response;
        }

        if (sellerOptional.isEmpty()) {
            response.put("success", false);
            response.put("message", "Seller username not found");
            return response;
        }

        User buyer = buyerOptional.get();
        User seller = sellerOptional.get();

        Optional<Stock> stockOptional = stockRepository.findByNameIgnoreCase(request.getStockName().trim());
        if (stockOptional.isEmpty()) {
            response.put("success", false);
            response.put("message", "Stock not found");
            return response;
        }

        Stock stock = stockOptional.get();

        Optional<Holding> sellerHoldingOptional =
                holdingRepository.findByUserIdAndStockId(seller.getId(), stock.getId());

        if (sellerHoldingOptional.isEmpty() || sellerHoldingOptional.get().getQuantity() < request.getQuantity()) {
            response.put("success", false);
            response.put("message", "Seller does not have enough stock quantity");
            return response;
        }

        if (buyer.getBalance() < orderValue) {
            response.put("success", false);
            response.put("message", "Buyer does not have enough balance");
            return response;
        }

        AdminOrder order = new AdminOrder();
        order.setBuyerTeam(buyerTeam);
        order.setSellerTeam(sellerTeam);
        order.setBuyerUserId(buyer.getId());
        order.setSellerUserId(seller.getId());
        order.setStockName(stock.getName());
        order.setPrice(request.getPrice());
        order.setQuantity(request.getQuantity());
        order.setOrderValue(orderValue);

        adminOrderRepository.save(order);

        response.put("success", true);
        response.put("message", "Order added successfully");
        response.put("buyerUsername", buyer.getUsername());
        response.put("sellerUsername", seller.getUsername());
        response.put("stockName", stock.getName());
        response.put("orderValue", orderValue);
        return response;
    }

    @PostMapping("/process")
    public Map<String, Object> processOrders() {
        Map<String, Object> response = new HashMap<>();

        List<AdminOrder> orders = adminOrderRepository.findAll()
                .stream()
                .filter(o -> "PENDING".equals(o.getStatus()))
                .collect(Collectors.toList());
        if (orders.isEmpty()) {
            response.put("success", false);
            response.put("message", "No orders available to process");
            return response;
        }

        priceUpdateRepository.deleteAll();

        Map<String, List<AdminOrder>> grouped = orders.stream()
                .collect(Collectors.groupingBy(AdminOrder::getStockName));

        for (Map.Entry<String, List<AdminOrder>> entry : grouped.entrySet()) {
            String stockName = entry.getKey();
            List<AdminOrder> stockOrders = entry.getValue();

            Optional<Stock> stockOptional = stockRepository.findByNameIgnoreCase(stockName);
            if (stockOptional.isEmpty()) {
                continue;
            }

            Stock stock = stockOptional.get();

            double calculatedNewPrice = marketBrainService.calculateNewPrice(stock, stockOrders);

            PriceUpdate preview = new PriceUpdate();
            preview.setStockId(stock.getId());
            preview.setStockName(stock.getName());
            preview.setOldPrice(stock.getPrice());
            preview.setNewPrice(calculatedNewPrice);

            priceUpdateRepository.save(preview);
        }

        response.put("success", true);
        response.put("message", "Preview generated successfully");
        return response;
    }

    @GetMapping("/preview")
    public List<PriceUpdate> getPreviewPrices() {
        return priceUpdateRepository.findAll();
    }

    @PostMapping("/approve")
    public Map<String, Object> approvePrices() {
        Map<String, Object> response = new HashMap<>();

        List<PriceUpdate> previews = priceUpdateRepository.findAll();
        List<AdminOrder> orders = adminOrderRepository.findAll()
                .stream()
                .filter(o -> "PENDING".equals(o.getStatus()))
                .collect(Collectors.toList());

        if (previews.isEmpty()) {
            response.put("success", false);
            response.put("message", "No preview prices available");
            return response;
        }

        for (PriceUpdate preview : previews) {
            Optional<Stock> stockOptional = stockRepository.findById(preview.getStockId());
            if (stockOptional.isPresent()) {
                Stock stock = stockOptional.get();
                stock.setPreviousPrice(stock.getPrice());
                stock.setPrice(preview.getNewPrice());
                stockRepository.save(stock);
            }
        }

        for (AdminOrder order : orders) {
            Optional<User> buyerOptional = userRepository.findById(order.getBuyerUserId());
            Optional<User> sellerOptional = userRepository.findById(order.getSellerUserId());
            Optional<Stock> stockOptional = stockRepository.findByNameIgnoreCase(order.getStockName());

            if (buyerOptional.isEmpty() || sellerOptional.isEmpty() || stockOptional.isEmpty()) {
                continue;
            }

            User buyer = buyerOptional.get();
            User seller = sellerOptional.get();
            Stock stock = stockOptional.get();

            double totalValue = order.getPrice() * order.getQuantity();

            Optional<Holding> sellerHoldingOptional =
                    holdingRepository.findByUserIdAndStockId(seller.getId(), stock.getId());

            if (sellerHoldingOptional.isEmpty()) {
                continue;
            }

            Holding sellerHolding = sellerHoldingOptional.get();
            if (sellerHolding.getQuantity() < order.getQuantity()) {
                continue;
            }

            if (buyer.getBalance() < totalValue) {
                continue;
            }

            // Update Seller Holding
            double sellerAvgPrice = sellerHolding.getTotalInvestment() / sellerHolding.getQuantity();
            double reductionValue = sellerAvgPrice * order.getQuantity();
            sellerHolding.setQuantity(sellerHolding.getQuantity() - order.getQuantity());
            sellerHolding.setTotalInvestment(Math.max(0, sellerHolding.getTotalInvestment() - reductionValue));
            holdingRepository.save(sellerHolding);

            // Update Buyer Holding
            Optional<Holding> buyerHoldingOptional =
                    holdingRepository.findByUserIdAndStockId(buyer.getId(), stock.getId());

            Holding buyerHolding = buyerHoldingOptional
                    .orElse(new Holding(buyer.getId(), stock.getId(), 0, 0));

            buyerHolding.setQuantity(buyerHolding.getQuantity() + order.getQuantity());
            buyerHolding.setTotalInvestment(buyerHolding.getTotalInvestment() + totalValue);
            holdingRepository.save(buyerHolding);

            buyer.setBalance(buyer.getBalance() - totalValue);
            seller.setBalance(seller.getBalance() + totalValue);

            userRepository.save(buyer);
            userRepository.save(seller);
        }

        priceUpdateRepository.deleteAll();

        // Mark all processed orders as EXECUTED (not deleted)
        for (AdminOrder order : orders) {
            order.setStatus("EXECUTED");
            adminOrderRepository.save(order);
        }

        response.put("success", true);
        response.put("message", "Prices approved and holdings updated successfully");
        return response;
    }

    @GetMapping("/user/{userId}")
    public List<Map<String, Object>> getOrdersByUserId(@PathVariable Long userId) {
        return adminOrderRepository.findAll()
                .stream()
                .filter(o -> o.getBuyerUserId().equals(userId) || o.getSellerUserId().equals(userId))
                .sorted(Comparator.comparing(AdminOrder::getId).reversed())
                .map(this::mapOrderResponse)
                .collect(Collectors.toList());
    }

    private Map<String, Object> mapOrderResponse(AdminOrder order) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", order.getId());
        data.put("buyerTeam", order.getBuyerTeam());
        data.put("sellerTeam", order.getSellerTeam());
        data.put("buyerUserId", order.getBuyerUserId());
        data.put("sellerUserId", order.getSellerUserId());
        data.put("stockName", order.getStockName());
        data.put("price", order.getPrice());
        data.put("quantity", order.getQuantity());
        data.put("orderValue", order.getOrderValue());
        data.put("status", order.getStatus());

        String buyerUsername = userRepository.findById(order.getBuyerUserId())
                .map(User::getUsername)
                .orElse(null);

        String sellerUsername = userRepository.findById(order.getSellerUserId())
                .map(User::getUsername)
                .orElse(null);

        data.put("buyerUsername", buyerUsername);
        data.put("sellerUsername", sellerUsername);

        return data;
    }
}