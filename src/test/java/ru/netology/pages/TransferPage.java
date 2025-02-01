package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;
import java.util.Random;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement amountField = $("[value]");
    private SelenideElement fromField = $("[placeholder]");
    private SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private SelenideElement error = $("[data-test-id='error-notification']");


    public TransferPage() {
        amountField.shouldBe(Condition.visible);
    }

    public DashboardPage reload(String amount, DataHelper.CardInfo cardInfo){
       transfer(amount, cardInfo);
       return new DashboardPage();
    }

    public void transfer(String amount, DataHelper.CardInfo cardInfo) {
        amountField.setValue(amount);
        fromField.setValue(cardInfo.getNumber());
        transferButton.click();

    }

    public void findError(String expectedText){
        error.shouldHave(Condition.text(expectedText), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }



}
