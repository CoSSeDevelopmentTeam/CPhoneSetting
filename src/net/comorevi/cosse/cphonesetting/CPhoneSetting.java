package net.comorevi.cosse.cphonesetting;

import cn.nukkit.Player;
import net.comorevi.cosse.cossephonecore.application.ApplicationBase;
import net.comorevi.cosse.cossephonecore.phone.CoSSePhone;

public class CPhoneSetting extends ApplicationBase {

    private static CPhoneSetting instance;

    public CPhoneSetting() {
        instance = this;
    }

    @Override
    public void startApplication(CoSSePhone phone, Player player) {
        phone.sendForm(player, FormProcessor.getMenu(phone, player));
    }

    public static CPhoneSetting getInstance() {
        return instance;
    }

}
