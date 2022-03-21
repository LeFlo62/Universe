package fr.leflodu62.universe.world.biome;

public class Biomes {
	
	public static final Biome PLAINS = new PlainBiome();
	public static final Biome MOUNTAINS = new MountainBiome();
	public static final Biome CAVE = new CaveBiome();
	
	public static final Biome TRANSITION = new TransitionBiome();
	
	
	public static Biome get(float temperature, float humidity) {
		if(MOUNTAINS.getTemperatureMin() <= temperature && temperature <= MOUNTAINS.getTemperatureMax() && MOUNTAINS.getHumidityMin() <= humidity && humidity <= MOUNTAINS.getHumidityMax()) {
			return MOUNTAINS;
		}
		if(PLAINS.getTemperatureMin() <= temperature && temperature <= PLAINS.getTemperatureMax() && PLAINS.getHumidityMin() <= humidity && humidity <= PLAINS.getHumidityMax()) {
			return PLAINS;
		}
		return PLAINS;
	}

}
