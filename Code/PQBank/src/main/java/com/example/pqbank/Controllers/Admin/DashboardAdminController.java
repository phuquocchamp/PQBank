package com.example.pqbank.Controllers.Admin;

import com.example.pqbank.Models.Model;
import com.example.pqbank.Models.Transaction;
import com.example.pqbank.Views.TransactionCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class DashboardAdminController implements Initializable {
    public ListView<Transaction> lastTransaction_listView;
    public LineChart<String, Double> barChart;
    private final ObservableList<Transaction> transactionsData = FXCollections.observableArrayList();
    public Label dateTime_lbl;
    public Label totalTransaction_lbl;
    public Label accountNumbers_lbl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAllTransactionsData();
        lastTransaction_listView.setItems(Model.getInstance().getAllTransactionAdmin());
        lastTransaction_listView.setCellFactory(e -> new TransactionCellFactory());
        initAllTransactionBarChart();
        showBarChart();
        bindData();

    }

    private void bindData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateTime_lbl.setText(LocalDate.now().format(formatter));
        int totalTrans = Model.getInstance().getDatabaseDriver().getTotalTransactions();
        totalTransaction_lbl.setText(String.valueOf(totalTrans));
        int totalNum = Model.getInstance().getDatabaseDriver().getNumberAccount();
        accountNumbers_lbl.setText(String.valueOf(totalNum));
    }

    public void initAllTransactionsData(){
        if(Model.getInstance().getAllTransactionAdmin().isEmpty()){
            Model.getInstance().setAllTransactionAdmin();
        }
    }

    public void initAllTransactionBarChart(){
        transactionsData.addAll(Model.getInstance().getAllTransactionAdmin());

    }
    public void showBarChart() {
        // Tạo các đối tượng XYChart.Series để chứa dữ liệu
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName("Transaction Amount : $");

        // Tính ngày bắt đầu và kết thúc của khoảng thời gian 7 ngày gần nhất
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);

        // Đếm số lượng giao dịch trong khoảng thời gian đó
        Map<String, Double> dailyTransactionAmount = new HashMap<>();
        for (Transaction transaction : transactionsData) {
            LocalDate transDate = transaction.dateProperty().get();
            if (transDate.isAfter(startDate.minusDays(1)) && transDate.isBefore(endDate.plusDays(1))) {
                String date = transDate.format(DateTimeFormatter.ofPattern("dd/MM"));
                double amount = transaction.amountMoneyProperty().get();
                dailyTransactionAmount.merge(date, amount, Double::sum);
            }
        }

        // Thêm dữ liệu vào series
        while (!startDate.isAfter(endDate)) {
            String date = startDate.format(DateTimeFormatter.ofPattern("dd/MM"));
            double amount = dailyTransactionAmount.getOrDefault(date, 0.0);
            series.getData().add(new XYChart.Data<>(date, amount));
            startDate = startDate.plusDays(1);
        }

        // Adding XYChart.Series to barchart.
        barChart.getData().add(series);
    }


}
