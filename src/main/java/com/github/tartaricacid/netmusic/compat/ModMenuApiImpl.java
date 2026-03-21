package com.github.tartaricacid.netmusic.compat;

import com.github.tartaricacid.netmusic.config.NetMusicConfigs;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuApiImpl implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> NetMusicConfigs.getInstance().getConfigScreen(parent);
	}
}
