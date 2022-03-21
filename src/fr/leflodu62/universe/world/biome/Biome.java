package fr.leflodu62.universe.world.biome;

public class Biome {

	protected float humidityMin;
	protected float humidityMax;
	protected float temperatureMin;
	protected float temperatureMax;
	
	protected float xScale = 10f;
	protected float yScale = 10f;
	protected float zScale = 10f;
	
	protected float maxHeight = 1f;
	
	public Biome(float humidityMin, float humidityMax, float temperatureMin, float temperatureMax) {
		this.humidityMin = humidityMin;
		this.humidityMax = humidityMax;
		this.temperatureMin = temperatureMin;
		this.temperatureMax = temperatureMax;
	}

	public float getHumidityMin() {
		return humidityMin;
	}

	public float getHumidityMax() {
		return humidityMax;
	}

	public float getTemperatureMin() {
		return temperatureMin;
	}

	public float getTemperatureMax() {
		return temperatureMax;
	}

	public float getXScale() {
		return xScale;
	}

	public float getYScale() {
		return yScale;
	}

	public float getZScale() {
		return zScale;
	}

	public float getMaxHeight() {
		return maxHeight;
	}

}
