package fr.leflodu62.universe.utils;

import java.text.NumberFormat;

import org.joml.Vector3f;

public class ColorBlender {
	
	public static Vector3f blendColors(float[] fractions, Vector3f[] colors, float progress) {
		Vector3f color = null;
		if(progress < 0) progress = 0;
		if(progress > 1) progress = 1;
        if (fractions != null) {
            if (colors != null) {
                if (fractions.length == colors.length) {
                    int[] indicies = getFractionIndicies(fractions, progress);
                    float[] range = new float[]{fractions[indicies[0]], fractions[indicies[1]]};
                    Vector3f[] colorRange = new Vector3f[]{colors[indicies[0]], colors[indicies[1]]};

                    float max = range[1] - range[0];
                    float value = progress - range[0];
                    float weight = value / max;

                    color = blend(colorRange[0], colorRange[1], 1f - weight);
                } else {
                    throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
                }
            } else {
                throw new IllegalArgumentException("Colours can't be null");
            }
        } else {
            throw new IllegalArgumentException("Fractions can't be null");
        }
        return color;
    }

    public static int[] getFractionIndicies(float[] fractions, float progress) {
        int[] range = new int[2];

        int startPoint = 0;
        while (startPoint < fractions.length && fractions[startPoint] <= progress) {
            startPoint++;
        }

        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }

        range[0] = startPoint - 1;
        range[1] = startPoint;

        return range;
    }

    public static Vector3f blend(Vector3f color1, Vector3f color2, double ratio) {
        float r = (float) ratio;
        float ir = (float) 1.0 - r;

        float red = color1.x* r + color2.x * ir;
        float green = color1.y * r + color2.y * ir;
        float blue = color1.z * r + color2.z * ir;

        if (red < 0) {
            red = 0;
        } else if (red > 1) {
            red = 1;
        }
        if (green < 0) {
            green = 0;
        } else if (green > 1) {
            green = 1;
        }
        if (blue < 0) {
            blue = 0;
        } else if (blue > 1) {
            blue = 1;
        }

        Vector3f color = null;
        try {
            color = new Vector3f(red, green, blue);
        } catch (IllegalArgumentException exp) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            System.out.println(nf.format(red) + "; " + nf.format(green) + "; " + nf.format(blue));
            exp.printStackTrace();
        }
        return color;
    }

}
