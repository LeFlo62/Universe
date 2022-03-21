package fr.leflodu62.universe.render.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector3f;

import de.javagl.obj.FloatTuple;
import de.javagl.obj.Mtl;
import de.javagl.obj.MtlReader;
import de.javagl.obj.Mtls;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjSplitting;
import de.javagl.obj.ObjUtils;
import fr.leflodu62.universe.Universe;
import fr.leflodu62.universe.render.texture.Texture;
import fr.leflodu62.universe.render.texture.TextureLoader;

public class ObjLoader {

	private ModelLoader modelLoader;
	private TextureLoader textureLoader;
	
	public ObjLoader(ModelLoader modelLoader, TextureLoader textureLoader) {
		this.modelLoader = modelLoader;
		this.textureLoader = textureLoader;
	}


	public ComplexModel loadObj(String file) {
		try {
			Obj main = ObjReader.read(Universe.class.getResourceAsStream("/assets/models/" + file));

			Map<String, Mtl> materials = new HashMap<>();
			
			for(String matFile : main.getMtlFileNames()) {
				List<Mtl> mats = MtlReader.read(Universe.class.getResourceAsStream("/assets/materials/" + matFile));
				mats.forEach(mat -> materials.put(mat.getName(), mat));
			}
			
			List<TexturedModel> models = new ArrayList<>();
			
			Map<String, Obj> objs = ObjSplitting.splitByMaterialGroups(main);

			for(String matName : objs.keySet()) {
				Mtl material;
				
				if(materials.containsKey(matName)) {
					material = materials.get(matName);
				} else {
					material = Mtls.create("default");
				}
				
				Texture texture;
				if(material.getMapKd() == null) {
					texture = TextureLoader.DEFAULT_TEXTURE;
				} else {
					texture = textureLoader.loadTexture(material.getMapKd());
				}
				Obj obj = ObjUtils.convertToRenderable(objs.get(matName));
				
				float[] positions = ObjData.getVerticesArray(obj);
				float[] textureCoords = ObjData.getTexCoordsArray(obj, 2);
				float[] normals = ObjData.getNormalsArray(obj);
				int[] indices = ObjData.getFaceNormalIndicesArray(obj);
				
				RawModel model = modelLoader.loadTexturedModelToVAO(positions, textureCoords, normals, indices);
				
				models.add(new TexturedModel(model, texture, material));
			}
			
			ComplexModel complexModel = new ComplexModel(models.toArray(new TexturedModel[models.size()]));
			FloatTuple[] fp = main.getFurthesPoints();
			complexModel.setSize(new Vector3f(Math.abs(fp[0].getX() - fp[1].getX()), Math.abs(fp[0].getY() - fp[1].getY()), Math.abs(fp[0].getZ() - fp[1].getZ())));
			
			Vector3f smallestPoint = new Vector3f(fp[0].getX(), fp[0].getY(), fp[0].getZ());
			complexModel.setSmallestPoint(smallestPoint);
			
			return complexModel;
		} catch (Exception e) {
			System.err.println("An error occured while laoding a model !");
			e.printStackTrace();
			System.exit(-1);
		}
		
		return null;
	}
	
}
