package ru.project.buySellStore.dto;

public class ReportDTO {
    private String category;

    public ReportDTO(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
