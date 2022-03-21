package fr.leflodu62.universe.world;

import java.util.Random;

public class SimplexNoise {
	//https://github.com/stegu/webgl-noise/tree/master/src
	SimplexNoiseOctave[] octaves;
	double[] frequencies;
	double[] amplitudes;

	double persistence;
	long seed;

	public SimplexNoise(int numberOfOctaves, double persistence, long seed) {
		this.persistence = persistence;
		this.seed = seed;

		// recieves a number (eg 128) and calculates what power of 2 it is (eg 2^7)
		octaves = new SimplexNoiseOctave[numberOfOctaves];
		frequencies = new double[numberOfOctaves];
		amplitudes = new double[numberOfOctaves];

		Random rnd = new Random(seed);

		for (int i = 0; i < numberOfOctaves; i++) {
			octaves[i] = new SimplexNoiseOctave(rnd.nextInt());

			frequencies[i] = Math.pow(2, i);
			amplitudes[i] = Math.pow(persistence, octaves.length - i);

		}

	}
	
	public SimplexNoise(int numberOfOctaves, double[] frequencies, double[] amplitudes, long seed) {
		this.seed = seed;

		// recieves a number (eg 128) and calculates what power of 2 it is (eg 2^7)
		octaves = new SimplexNoiseOctave[numberOfOctaves];
		this.frequencies = frequencies;
		this.amplitudes = amplitudes;

		Random rnd = new Random(seed);

		for (int i = 0; i < numberOfOctaves; i++) {
			octaves[i] = new SimplexNoiseOctave(rnd.nextLong());
		}
	}

	public double getNoise(double x, double y) {

		double result = 0;
		
		for (int i = 0; i < octaves.length; i++) {
			result = result + octaves[i].noise(x / frequencies[i], y / frequencies[i]) * amplitudes[i];
		}

		return result;

	}

	public double getNoise(double x, double y, double z) {

		double result = 0;

		for (int i = 0; i < octaves.length; i++) {
			result = result + octaves[i].noise(x / frequencies[i], y / frequencies[i], z / frequencies[i]) * amplitudes[i];
		}

		return result;
	}
	
	public double getNoise2(double x, double y) {

		double result = 0;
		double a = 0;
		
		for (int i = 0; i < octaves.length; i++) {
			result = result + octaves[i].noise(x * frequencies[i], y * frequencies[i]) * amplitudes[i];
			a += amplitudes[i];
		}

		return result/a;

	}

	public double getNoise2(double x, double y, double z) {

		double result = 0;
		double a = 0;

		for (int i = 0; i < octaves.length; i++) {
			result = result + octaves[i].noise(x * frequencies[i], y * frequencies[i], z * frequencies[i]) * amplitudes[i];
			a += amplitudes[i];
		}

		return result/a;

	}
}
