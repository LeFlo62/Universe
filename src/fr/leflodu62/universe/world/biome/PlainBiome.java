package fr.leflodu62.universe.world.biome;

public class PlainBiome extends Biome{

	public PlainBiome() {
		super(0.4f, 0.6f, 15f, 29f);
		this.xScale = this.zScale = 10f;
		this.maxHeight = 10f;
	}

}
