package net.comorevi.cosse.cphonesetting;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import itsu.mcbe.form.base.CustomForm;
import itsu.mcbe.form.base.SimpleForm;
import itsu.mcbe.form.element.Button;
import itsu.mcbe.form.element.Label;
import itsu.mcbe.form.element.Toggle;
import net.comorevi.cosse.cossephonecore.application.ApplicationManager;
import net.comorevi.cosse.cossephonecore.phone.CoSSePhone;

import java.util.List;

public class FormProcessor {

    public static SimpleForm getMenu(CoSSePhone phone, Player player) {
        SimpleForm form = new SimpleForm() {
            @Override
            public void onEnter(Player player, int index) {
                switch (index) {
                    case 0: {
                        phone.sendForm(player, getApplicationMenu(phone, player));
                        break;
                    }
                }
            }
        }

            .setId(FormIDs.ID_MENU)
            .setTitle("設定")
            .addButton(new Button("アプリケーション"));

        return form;
    }

    public static SimpleForm getApplicationMenu(CoSSePhone phone, Player player) {
        SimpleForm form = new SimpleForm()
            .setId(FormIDs.ID_APPMENU)
            .setTitle("設定");

        for (String app : ApplicationManager.getPlayerApplications(player.getName())) {
            form.addButton(new Button(app) {
                @Override
                public void onClick(Player player) {
                    phone.sendForm(player, getApplicationSettingForm(phone, player, app));
                }
            });
        }

        return form;
    }

    public static CustomForm getApplicationSettingForm(CoSSePhone phone, Player player, String appName) {
        CustomForm form = new CustomForm() {
            @Override
            public void onEnter(Player player, List<Object> response) {
                if (!(boolean) response.get(0)) {
                    phone.pushNotify(player, CPhoneSetting.getInstance().getApplicationDescription().getName(), appName + "の通知を無効にしました。");
                    phone.addOffPush(player, appName);

                } else {
                    phone.pushNotify(player, CPhoneSetting.getInstance().getApplicationDescription().getName(), appName + "の通知を有効にしました。");
                    phone.removeOffPush(player, appName);

                }

                if ((boolean) response.get(1) && !ApplicationManager.getApplicationByName(appName).getApplicationDescription().isDefault()) {
                    ApplicationManager.unInstallApplication(player.getName(), appName);
                    phone.pushNotify(player, CPhoneSetting.getInstance().getApplicationDescription().getName(), appName + "をアンインストールしました。");
                }
            }
        }

            .setId(FormIDs.ID_APPSETTING)
            .setTitle("設定")
            .addFormElement(new Toggle("通知", !phone.isOffPush(player, appName)));

        if (ApplicationManager.getApplicationByName(appName).getApplicationDescription().isDefault()) {
            form.addFormElement(new Label(TextFormat.ITALIC + "このアプリケーションはデフォルトでインストールされているため、アンインストールは無効です。"));

        } else {
            form.addFormElement(new Toggle(TextFormat.RED + "アンインストール", false));
            
        }

        return form;
    }
}
