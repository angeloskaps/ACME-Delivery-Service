package com.acme.eshop.util;

import com.acme.eshop.AcmeApplication;
import com.acme.eshop.model.*;
import com.acme.eshop.repository.OrderItemRepository;
import com.acme.eshop.repository.OrderRepository;
import com.acme.eshop.service.OrderService;
import com.acme.eshop.service.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
    private static final OrderService orderService = new OrderServiceImpl(new OrderRepository(), new OrderItemRepository());
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static Order generateOrder(List<Product> allProducts, List<Customer> allCustomers) {
        List<Product> randomProducts  = selectRandomProducts(allProducts);
        Customer randomCustomer = selectRandomCustomer(allCustomers);
        PaymentMethod paymentMethod = selectRandomPaymentMethod();
        Order order=  orderService.createOrder(randomProducts, randomCustomer ,paymentMethod);
        return order;
    }

    static PaymentMethod selectRandomPaymentMethod() {
        Random random = new Random();
        int randomNum = random.nextInt(2);
        if (randomNum == 0) {
            return PaymentMethod.CARD;
        } else if (randomNum == 1) {
            return PaymentMethod.CASH;
        } else {
            return PaymentMethod.WIRE_TRANSFER;
        }

    }

    static Customer selectRandomCustomer(List<Customer> allCustomers) {
        Random random = new Random();
        int randomCustomerIndex = random.nextInt(allCustomers.size() - 1);
        return allCustomers.get(randomCustomerIndex);
    }

    static List<Product> selectRandomProducts(List<Product> allProducts) {
        Random random = new Random();
        List<Product> selectedProducts = new ArrayList<>();
        int numOfSelectedProducts = random.nextInt(allProducts.size() - 1) + 1;
        for (int i = 0; i < numOfSelectedProducts; i++) {
            int index = random.nextInt(allProducts.size() - 1);
            selectedProducts.add(allProducts.get(index));
        }
        return selectedProducts;
    }

    public static List<Customer> createCustomerSample() {
        // @formatter:off
        List<Customer> customers = List.of(
                Customer.builder().name("Thanos Sam").username("Nossam").password("1231123").address("Venizelou 12").email("test@test.com").customerCategory(CustomerCategory.B2B).build(),
                Customer.builder().name("Argiris").username("Argy").password("5645435345").address("Venizelou 13").email("notest@test.com").customerCategory(CustomerCategory.B2G).build(),
                Customer.builder().name("Nikos").username("NikTheGreek").password("345345").address("Venizelou 14").email("dummy@test.com").customerCategory(CustomerCategory.B2C).build(),
                Customer.builder().name("Maria").username("Marion123").password("1231").address("Venizelou 15").email("sandbox@test.com").customerCategory(CustomerCategory.B2G).build(),
                Customer.builder().name("Dimitra").username("Dimi").password("678678").address("Venizelou 16").email("noemail@test.com").customerCategory(CustomerCategory.B2G).build(),
                Customer.builder().name("Euagelia").username("Eua123").password("12fdsfd31123").address("Venizelou 17").email("emailname@test.com").customerCategory(CustomerCategory.B2G).build(),
                Customer.builder().name("Viktoria").username("Vik").password("fhh45345").address("Venizelou 18").email("emailer@test.com").customerCategory(CustomerCategory.B2G).build(),
                Customer.builder().name("Aggelos").username("Angel").password("dgdg24").address("Venizelou 19").email("devemail@test.com").customerCategory(CustomerCategory.B2G).build(),
                Customer.builder().name("Takis").username("Takis123").password("dfhdh234").address("Venizelou 21").email("passemail@test.com").customerCategory(CustomerCategory.B2G).build(),
                Customer.builder().name("Nikos Koukos").username("FOUSEKIS").password("sdfs1214d1").address("Venizelou 23").email("pseudoemail@test.com").customerCategory(CustomerCategory.B2G).build(),
                Customer.builder().name("Thesalia").username("Thess").password("d1224tgbb").address("Venizelou 24").email("fakee_email@test.com").customerCategory(CustomerCategory.B2G).build(),
                Customer.builder().name("Nikitas").username("niki12").password("343hb54").address("Venizelou 26").email("gamil@test.com").customerCategory(CustomerCategory.B2G).build(),
                Customer.builder().name("Kostas").username("Kotsos1").password("bvfb456").address("Venizelou 27").email("amazon@test.com").customerCategory(CustomerCategory.B2G).build(),
                Customer.builder().name("Mpampis").username("Kyfonidis").password("fg343").address("Venizelou 25").email("verizon@test.com").customerCategory(CustomerCategory.B2G).build(),
                Customer.builder().name("Sergios").username("Serj").password("gfdj456357ad").address("Venizelou 28").email("nopo@test.com").customerCategory(CustomerCategory.B2G).build());

        // @formatter:on
        for (final Customer customer : customers) {
            logger.info("{}", customer);
        }
        return customers;
    }

    public static List<Product> createProductSample() {

        List<Product> products = List.of(
                Product.builder().name("Samsung A5").price(BigDecimal.valueOf(230.50)).type("Smartphone").build(),
                Product.builder().name("Razer Deathadder").price(BigDecimal.valueOf(25.50)).type("Mouse").build(),
                Product.builder().name("Hitachi AG43").price(BigDecimal.valueOf(230.50)).type("A/C").build(),
                Product.builder().name("Samsung Galaxy S20").price(BigDecimal.valueOf(1000.10)).type("Smartphone").build(),
                Product.builder().name("iPhone 13 Pro").price(BigDecimal.valueOf(1230)).type("Smartphone").build(),
                Product.builder().name("Dell Inspiron 4450").price(BigDecimal.valueOf(650.50)).type("Laptop").build(),
                Product.builder().name("Nikkon G54").price(BigDecimal.valueOf(230.50)).type("DSLR Camera").build(),
                Product.builder().name("CoolerMaster Thrust").price(BigDecimal.valueOf(50)).type("Keyboard").build(),
                Product.builder().name("GoPro 7").price(BigDecimal.valueOf(690)).type("Action Camera").build(),
                Product.builder().name("Xiaomi SmartMic").price(BigDecimal.valueOf(260.50)).type("Microphone").build(),
                Product.builder().name("Razer 34").price(BigDecimal.valueOf(150.12)).type("headset").build(),
                Product.builder().name("Samsung A4").price(BigDecimal.valueOf(330.50)).type("Smartphone").build(),
                Product.builder().name("iPhone 6").price(BigDecimal.valueOf(530.50)).type("Smartphone").build(),
                Product.builder().name("RX570 Sapphire Nitro").price(BigDecimal.valueOf(330.50)).type("GPU").build()
        );

        for (final Product product : products)
            logger.info("{}", product);

        return products;
    }


}
