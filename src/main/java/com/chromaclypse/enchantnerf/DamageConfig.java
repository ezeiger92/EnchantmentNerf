package com.chromaclypse.enchantnerf;

import java.util.Map;

import com.chromaclypse.api.Defaults;
import com.chromaclypse.api.config.ConfigObject;

public class DamageConfig extends ConfigObject {

	public Map<String, Double> materialScales = Defaults.EmptyMap();
	public Map<String, Map<String, Double>>  enchantmentDamage = Defaults.EmptyMap();
}
