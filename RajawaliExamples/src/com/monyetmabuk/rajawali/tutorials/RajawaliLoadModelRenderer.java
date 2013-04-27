package com.monyetmabuk.rajawali.tutorials;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rajawali.BaseObject3D;
import rajawali.animation.Animation3D;
import rajawali.animation.RotateAnimation3D;
import rajawali.animation.RotateAroundAnimation3D;
import rajawali.filters.TouchRippleFilter;
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

public class RajawaliLoadModelRenderer extends RajawaliRenderer{
	private PointLight mLight;
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
		//mPicker = new ObjectColorPicker(this);
		//mPicker.setOnObjectPickedListener(this);
		
		mFilter = new TouchRippleFilter();
		mFilter.setRippleSize(25);
		mPostProcessingRenderer.setQuadSegments(QUAD_SEGMENTS);
		mPostProcessingRenderer.setQuality(PostProcessingQuality.LOW);
		addPostProcessingFilter(mFilter);
		
		mLight = new PointLight();
		mLight.setPosition(0, 0, -4);
		mLight.setPower(7);
		mCamera.setLookAt(0, 0, 0);
		mCamera.setZ(-30);

		ObjParser objParser = new ObjParser(mContext.getResources(), mTextureManager, R.raw.fire_truck_obj);
		objParser.parse();
		mObjectGroup = objParser.getParsedObject();
		mObjectGroup.addLight(mLight);
		addChild(mObjectGroup);
		
		SimpleMaterial planeMat = new SimpleMaterial();
		Bitmap texture = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.space);
		planeMat.addTexture(mTextureManager.addTexture(texture));
		Plane plane = new Plane(4, 4, 1, 1);
		plane.setRotZ(-90);
		plane.setScale(3.7f);
		plane.setMaterial(planeMat);
		addChild(plane);
		
		/* MD2 Animation*/

		mCameraAnim = new RotateAnimation3D(Axis.Y, 360);
		mCameraAnim.setDuration(8000);
		mCameraAnim.setRepeatCount(Animation3D.INFINITE);
		mCameraAnim.setTransformable3D(mObjectGroup);

		mLightAnim = new RotateAroundAnimation3D(new Number3D(), Axis.Z, 10);
		mLightAnim.setDuration(3000);
		mLightAnim.setRepeatCount(Animation3D.INFINITE);
		mLightAnim.setTransformable3D(mLight);
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		((RajawaliExampleActivity) mContext).showLoader();
		super.onSurfaceCreated(gl, config);	
		((RajawaliExampleActivity) mContext).hideLoader();
		mCameraAnim.start();
		mLightAnim.start();		
	}
	
	public void stopRotation(){
		mCameraAnim.cancel();
		mLightAnim.cancel();	
	}
	
	public void startRotation(){
		mCameraAnim.start();
		mLightAnim.start();	
	}
	
	public void manualRotation(float xBegin, float xFinish, float yBegin, float yFinish){
		//manualRotation = new RotateAnimation3D(Axis.X, xBegin, );
		
		mObjectGroup.setRotation(((xFinish-xBegin)* TOUCH_SCALE_FACTOR), ((yFinish-yBegin)* TOUCH_SCALE_FACTOR), 0);
	}

	public void onDrawFrame(GL10 glUnused) {
		super.onDrawFrame(glUnused);
		mFilter.setTime((float) frameCount++ *.05f);//ripples
	}
	
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		super.onSurfaceChanged(gl, width, height);
		mFilter.setScreenSize(width, height);
	}
	
	public void setTouch(float x, float y) {
		mFilter.addTouch(x, y, frameCount *.05f);
	}
	
}
