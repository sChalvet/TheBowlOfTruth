package com.monyetmabuk.rajawali.tutorials;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rajawali.BaseObject3D;
import rajawali.animation.Animation3D;
import rajawali.animation.RotateAnimation3D;
import rajawali.animation.RotateAroundAnimation3D;
import rajawali.filters.TouchRippleFilter;
import rajawali.lights.ALight;
import rajawali.lights.DirectionalLight;
import rajawali.lights.PointLight;
import rajawali.materials.DiffuseMaterial;
import rajawali.materials.SimpleMaterial;
import rajawali.math.Number3D;
import rajawali.math.Number3D.Axis;
import rajawali.parser.ObjParser;
import rajawali.primitives.Cube;
import rajawali.primitives.Plane;
import rajawali.renderer.RajawaliRenderer;
import rajawali.renderer.PostProcessingRenderer.PostProcessingQuality;
import rajawali.util.ObjectColorPicker;
import rajawali.util.OnObjectPickedListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

/**
 * 
 * @author Dennis Ippel, modified by Jack Fortenbery and Samuel Chalvet.
 * 
 *  This Activity loads the bowl of truth and the starry background.
 *  It also process' commands to stop spinning the bowl and move it manually.
 *  It also calls the rippleRender to create the wave effect.
 *
 */
public class RajawaliLoadModelRenderer extends RajawaliRenderer{
	//private PointLight mLight;
	private DirectionalLight mLight;
	private BaseObject3D mObjectGroup;
	private Animation3D mCameraAnim, mLightAnim, manualRotation;
	private ObjectColorPicker mPicker;
	public boolean rotate = true;
	
	//for ripple effect
	private TouchRippleFilter mFilter;
	private long frameCount;
	private final int QUAD_SEGMENTS = 40;
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	
	public RajawaliLoadModelRenderer(Context context) {
		super(context);
		setFrameRate(60);
		
	}

	protected void initScene() {
		
		//instantiates the ripple fileter for the waves
		mFilter = new TouchRippleFilter();
		mFilter.setRippleSize(25);
		mPostProcessingRenderer.setQuadSegments(QUAD_SEGMENTS);
		mPostProcessingRenderer.setQuality(PostProcessingQuality.LOW);
		addPostProcessingFilter(mFilter);
		
		//instantiates a directional light pointed at the bowl
		mLight = new DirectionalLight();
		mLight.setPower(3);
		mLight.setPosition(0, 0, -150);
		mLight.setColor(1, 1, 1);
		
		//sets camera parameters
		mCamera.setLookAt(0, 0, 0);
		mCamera.setZ(-100);
		mCamera.setFarPlane(160f);

		//instantiates the bowl
		ObjParser objParser = new ObjParser(mContext.getResources(), mTextureManager, R.raw.bowl_of_truth_obj2);
		objParser.parse();
		mObjectGroup = objParser.getParsedObject();
		//adds light to the bowl
		mObjectGroup.addLight(mLight);
		addChild(mObjectGroup);
		
		//creates a plane textured with a starry sky
		SimpleMaterial planeMat = new SimpleMaterial();
		Bitmap texture = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.space);
		planeMat.addTexture(mTextureManager.addTexture(texture));
		Plane plane = new Plane(12, 12, 1, 1);
		plane.setRotZ(0);
		plane.setZ(40);
		plane.setScale(10f);
		plane.setMaterial(planeMat);
		addChild(plane);
		
		
		//rotates the object
		mCameraAnim = new RotateAnimation3D(Axis.Y, 360);
		mCameraAnim.setDuration(8000);
		mCameraAnim.setRepeatCount(Animation3D.INFINITE);
		mCameraAnim.setTransformable3D(mObjectGroup);
	}
	
	//shows the spinning boal while the object loads
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		((RajawaliExampleActivity) mContext).showLoader();
		super.onSurfaceCreated(gl, config);	
		((RajawaliExampleActivity) mContext).hideLoader();
		mCameraAnim.start();	
	}
	
	//called when button btnChange is pressed, stops the rotation of the bowl
	public void stopRotation(){
		mCameraAnim.cancel();	
	}
	
	//called when button btnChange is pressed, starts the rotation of the bowl
	public void startRotation(){
		mCameraAnim.start();
	}
	
	//rotates the object, called from onTouch method
	public void manualRotation(float xBegin, float xFinish, float yBegin, float yFinish){
		
		mObjectGroup.setRotation(((yFinish-yBegin)* TOUCH_SCALE_FACTOR), ((xFinish-xBegin)* TOUCH_SCALE_FACTOR), 0);
	}

	//sets the amount of time the waves last
	public void onDrawFrame(GL10 glUnused) {
		super.onDrawFrame(glUnused);
		mFilter.setTime((float) frameCount++ *.05f);//ripples
	}
	
	//resizes the screen
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		super.onSurfaceChanged(gl, width, height);
		mFilter.setScreenSize(width, height);
	}
	
	//calls the waves rendere centered on the points passed here
	public void setTouch(float x, float y) {
		mFilter.addTouch(x, y, frameCount *.05f);
	}
	
}
