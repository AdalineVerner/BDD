package ru.netology.tests;

import com.codeborne.selenide.Selenide;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;
import ru.netology.pages.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {
    DashboardPage dashboardPage;
    DataHelper.CardInfo firstCardInfo;
    DataHelper.CardInfo secondCardInfo;
    int firstCardBalance;
    int secondCardBalance;


    @BeforeEach
    void setUp() {
        DataHelper.VerificationCode code = DataHelper.getVerificationCode();
        var user = DataHelper.getAuthInfo();
        var loginPage = open("http://localhost:9999/", LoginPage.class);
        var verificationPage = loginPage.validLogin(user);
        dashboardPage = verificationPage.validVerify(code);
        firstCardInfo = DataHelper.getFirstCardInfo();
        secondCardInfo = DataHelper.getSecondCardInfo();
        firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
    }

    @Test
    public void shouldTransferFromSecondCardToFirstCard() {
        var transferPage = dashboardPage.chooseReplenishmentCard(firstCardInfo);
        var amount = DataHelper.getAmount(secondCardBalance);
        transferPage.reload(String.valueOf(amount), secondCardInfo);
        var expectedBalanceFirstCard = firstCardBalance + amount;
        var expectedBalanceSecondCard = secondCardBalance - amount;
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);
        Assertions.assertAll(
                () -> assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard),
                () -> assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard));
    }
    @Test
    public void shouldNoticeAboutErrorIfAmountMoreBalance(){
        var transferPage = dashboardPage.chooseReplenishmentCard(firstCardInfo);
        var amount = DataHelper.invalidAmount(secondCardBalance);
        transferPage.transfer(String.valueOf(amount), secondCardInfo);
        Assertions.assertAll(
                () -> transferPage.findError("Ошибка!"),
                () ->assertEquals(firstCardBalance, dashboardPage.getCardBalance(firstCardInfo)),
                () ->assertEquals(secondCardBalance, dashboardPage.getCardBalance(secondCardInfo)));

    }
}
