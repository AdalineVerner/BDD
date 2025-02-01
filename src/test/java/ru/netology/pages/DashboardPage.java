package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final SelenideElement heading =$(byText("Личный кабинет"));
    private final ElementsCollection cards = $$(".list__item div");
    private final SelenideElement reloadButton = $("[data-test-id ='action-reload']");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    public DashboardPage() {
        heading.shouldBe(Condition.visible);
    }
    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        val text = cards.findBy(Condition.attribute("data-test-id",cardInfo.getTestId())).getText();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
    public TransferPage chooseReplenishmentCard(DataHelper.CardInfo card){
        cards.findBy(Condition.attribute("data-test-id", card.getTestId())).$("button").click();
        return new TransferPage();
}




}
