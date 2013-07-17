//OMG LOOK I MADE SOME CHANGE

package com.example.sushirain;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.color.Color;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MainActivity extends BaseGameActivity implements SensorEventListener {


	private static final float WIDTH = 800;
	private static final float HEIGHT = 400;
	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mTiledTextureRegion;
	private AnimatedSprite animatedSprite;
	private Scene mScene;
	private Camera mCamera;
    private SensorManager sensorManager;

    private int accellerometerSpeedX;
    private int accellerometerSpeedY;
    private int sX, sY; // Sprite coordinates

	@Override
	public EngineOptions onCreateEngineOptions() {
		  // Define our mCamera object
		  mCamera = new Camera(0, 0, WIDTH, HEIGHT);
		  // Declare & Define our engine options to be applied to our Engine object
		  EngineOptions engineOptions = new EngineOptions(true,
		      ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
		      mCamera);

		  engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		  // Return the engineOptions object, passing it to the engine
		  
		  return engineOptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback){
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
			
			/* Create the bitmap texture atlas for the sprite's texture
		       region */
		           mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 200, 100, 
		        		   TextureOptions.BILINEAR);
		           /* Create the sprite's texture region via the
		       BitmapTextureAtlasTextureRegionFactory */
		           mTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, this, "pacman_sprite.png", 2, 1);
		           
		           try{
		           mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
		        		       } catch (TextureAtlasBuilderException e) {
		        		         e.printStackTrace();
		        		       }
		        		       
		           
		             
		           mBitmapTextureAtlas.load();   
	 		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback){
    mScene = new Scene();
    /* Add our marble sprite to the bottom left side of the Scene
    initially */
        animatedSprite = new AnimatedSprite(HEIGHT * 0.5f, WIDTH * 0.5f, mTiledTextureRegion, mEngine.getVertexBufferObjectManager());

//        /* Attach the marble to the Scene */
//        /* Length to play each frame before moving to the next */
//        long frameDuration[]={100,200,300,400,500};
//        /* We can define the indices of the animation to play between */
//        int firstTileIndex = 0;
//        int lastTileIndex = mTiledTextureRegion.getTileCount();
//        /* Allow the animation to continuously loop? */
//        boolean loopAnimation = true;
//        /* Animate the sprite with the data as set defined above */
        
        mScene.setBackground(new Background(Color.WHITE));
        animatedSprite.animate(300);
        
        mScene.attachChild(animatedSprite);
        
        //sensor stuff
        sensorManager = (SensorManager) this.getSystemService(this.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), sensorManager.SENSOR_DELAY_GAME);

        this.mEngine.registerUpdateHandler(new FPSLogger());
        this.mEngine.registerUpdateHandler(new IUpdateHandler() {
            public void onUpdate(float pSecondsElapsed) {
                updateSpritePosition();
            }

            public void reset() {
                // TODO Auto-generated method stub
            }
        });
    // Notify the callback that we're finished creating the scene, returning
    // mScene to the mEngine object (handled automatically)
    pOnCreateSceneCallback.onCreateSceneFinished(mScene);
		
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback){

	pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
    public void onLoadComplete() {

    }

    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    accellerometerSpeedX = (int)event.values[1];
                    accellerometerSpeedY = (int)event.values[0];
                    break;
            }
        }
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void updateSpritePosition() {
        if ((accellerometerSpeedX != 0) || (accellerometerSpeedY != 0)) {
            // Set the Boundary limits
            int tL = 0;
            int lL = 0;
            int rL = (int) (WIDTH - (int)animatedSprite.getWidth());
            int bL = (int) (HEIGHT - (int)animatedSprite.getHeight());

            // Calculate New X,Y Coordinates within Limits
            if (sX >= lL) sX += accellerometerSpeedX; else sX = lL;
            if (sX <= rL) sX += accellerometerSpeedX; else sX = rL;
            if (sY >= tL) sY += accellerometerSpeedY; else sY = tL;
            if (sY <= bL) sY += accellerometerSpeedY; else sY = bL;

            // Double Check That New X,Y Coordinates are within Limits
            if (sX < lL)      sX = lL;
            else if (sX > rL) sX = rL;
            if (sY < tL)      sY = tL;
            else if (sY > bL) sY = bL;

            animatedSprite.setPosition(sX, 50);
        }
    }
    
    //a new comment to test merging


}