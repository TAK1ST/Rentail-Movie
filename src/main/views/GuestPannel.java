/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.views;

import java.util.Map;
import static main.services.StatisticServices.*;

/**
 *
 * @author tak
 */
public class GuestPannel {

    public static void show() {
        introduction();
        showTopRevenueGeneratingMovies();
        showTopHighestRatedMovies();
        showTopMostWishlistedMovies();
        showTopPremiumCustomersBySpending();
        showTopMostActiveGenres();
    }

    public static void introduction() {
        System.out.println("Introduction");
        
        System.out.println();
    }

    public static void showTopRevenueGeneratingMovies() {
        Map<String, Double> top5RevenueGeneratingMovies = getTheMostRevenueGeneratingMovies();
        System.out.println(
                "The Most Revenue Generating Movies:");
        top5RevenueGeneratingMovies.forEach(
                (title, revenue)
                -> System.out.println("Movie: " + title + ", Revenue: $" + revenue)
        );
        System.out.println();
    }

    public static void showTopHighestRatedMovies() {
        Map<String, Double> top5HighestRatedMovies = getTheMostHighestRatedMovies();
        System.out.println("The Most Highest Rate Movie:");
        top5HighestRatedMovies.forEach((title, avg_rating)
                -> System.out.println("Movie: " + title + ", Review: " + avg_rating));
        System.out.println();
    }

    public static void showTopMostWishlistedMovies() {
        Map<String, Integer> top5MostWishlistedMovies = getTheMostWishlistedMovies();
        System.out.println("The Most Wishlist Movie: ");
        top5MostWishlistedMovies.forEach((title, wishlist_count)
                -> System.out.println("Movie:" + title + "Wishlist's Quantity : " + wishlist_count));
        System.out.println();
    }

    public static void showTopPremiumCustomersBySpending() {
        Map<String, Double> top5PremiumCustomersBySpending = getTheMostPremiumCustomersBySpending();
        System.out.println("The Most Premium Customer By Spending");
        top5PremiumCustomersBySpending.forEach((full_name, total_spent)
                -> System.out.println("User: " + full_name + "Spending:" + total_spent));
        System.out.println();
    }

    public static void showTopMostActiveGenres() {
        Map<String, Integer> top5MostActiveGenres = getTheMostActiveGenres();
        System.out.println("The Most Active Genres: ");
        top5MostActiveGenres.forEach((genre_name, rental_count) -> System.out.println("Genre: " + genre_name + "Rental's Quantity" + rental_count));
        System.out.println();
    }

}
