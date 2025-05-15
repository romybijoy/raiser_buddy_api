package com.project.raiserbuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Month;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesMonthDataDTO {

        private int year;
        private int month;
        private double totalSales;
        private String monthName;

        public SalesMonthDataDTO(int year, int month, double totalSales) {
            this.year = year;
            this.month = month;
            this.totalSales = totalSales;
            this.monthName = Month.of(month).name();
        }

        // Getters and setters

        public String getMonthName() {
            return monthName;
        }

        // If you need to get a more human-readable format
        public String getFormattedMonthName() {
            return monthName.charAt(0) + monthName.substring(1).toLowerCase();
        }


}
