/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.views;

import java.util.Map;
import static main.utils.statisticUtils.*;

/**
 *
 * @author tak
 */
public class GuestPannel {

    public static void getTopRevenueGeneratingMovies() {
        Map<String, Double> top5RevenueGeneratingMovies = getTop5RevenueGeneratingMovies();
        System.out.println(
                "Top 5 Revenue Generating Movies:");
        top5RevenueGeneratingMovies.forEach(
                (title, revenue)
                -> System.out.println("Movie: " + title + ", Revenue: $" + revenue)
        );
        System.out.println();
    }

    public static void getTopHighestRatedMovies() {
        Map<String, Double> top5HighestRatedMovies = getTop5HighestRatedMovies();
        System.out.println("Top 5 Highest Rate Movie:");
        top5HighestRatedMovies.forEach((title, avg_rating)
                -> System.out.println("Movie: " + title + ", Review: " + avg_rating));
        System.out.println();
    }

    public static void getTopMostWishlistedMovies() {
        Map<String, Integer> top5MostWishlistedMovies = getTop5MostWishlistedMovies();
        System.out.println("Top 5 Most Wishlist Movie: ");
        top5MostWishlistedMovies.forEach((title, wishlist_count)
                -> System.out.println("Movie:" + title + "Wishlist's Quantity : " + wishlist_count));
        System.out.println();
    }

    public static void getTopPremiumCustomersBySpending() {
        Map<String, Double> top5PremiumCustomersBySpending = getTop5PremiumCustomersBySpending();
        System.out.println("Top 5 Premium Customer By Spending");
        top5PremiumCustomersBySpending.forEach((full_name, total_spent)
                -> System.out.println("User: " + full_name + "Spending:" + total_spent));
        System.out.println();
    }

    public static void getTopMostActiveGenres() {
        Map<String, Integer> top5MostActiveGenres = getTop5MostActiveGenres();
        System.out.println("Top 5 Most Active Genres: ");
        top5MostActiveGenres.forEach((genre_name, rental_count) -> System.out.println("Genre: " + genre_name + "Rental's Quantity" + rental_count));
        System.out.println();
    }

}
