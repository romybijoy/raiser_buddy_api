package com.project.raiserbuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
public class SalesDailyDataDTO {

        private Date date;
        private double totalSales;

        public SalesDailyDataDTO(Date date, double totalSales) {
                this.date = date;
                this.totalSales = totalSales;
        }
}
