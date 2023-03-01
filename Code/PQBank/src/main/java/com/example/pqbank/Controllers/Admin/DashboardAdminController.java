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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private void showBarChart() {
        // Tạo các đối tượng XYChart.Series để chứa dữ liệu
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName("Transaction Amount");

        // Adding data into XYChart.Series
        for (int i = 1; i <= 7; i++) {
            double sum = 0.0;
            String[] date = new String[3];
            for (Transaction transaction : transactionsData) {
                if (transaction.dateProperty().get().getDayOfWeek().getValue() == i) {
                    sum += transaction.amountMoneyProperty().get();
                    date = transaction.dateProperty().get().toString().split("-");
                }
            }
            try {
                series.getData().add(new XYChart.Data<>(getDayOfWeek(i).concat(" - " + date[2]), sum));
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        // Adding XYChart.Series to barchart.
        barChart.getData().add(series);
    }

    private String getDayOfWeek(int dayOfWeek) {
        return switch (dayOfWeek) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6 -> "Saturday";
            case 7 -> "Sunday";
            default -> "";
        };
    }

}
