//OMG LOOK I MADE SOME CHANGE

package com.example.sushirain;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

public class MainActivity extends BaseGameActivity {


	private static final float WIDTH = 800;
	private static final float HEIGHT = 400;
	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mTiledTextureRegion;
	private Scene mScene;
	private Camera mCamera;

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
		           mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 256, 53, 
		        		   TextureOptions.BILINEAR);
		           /* Create the sprite's texture region via the
		       BitmapTextureAtlasTextureRegionFactory */
		           mTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, this, "sprite_sheet.png", 5, 1);
		           
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
        AnimatedSprite animatedSprite = new AnimatedSprite(HEIGHT * 0.5f, WIDTH * 0.5f, mTiledTextureRegion, mEngine.getVertexBufferObjectManager());
        /* Attach the marble to the Scene */
        /* Length to play each frame before moving to the next */
        long frameDuration[]={100,200,300,400,500};
        /* We can define the indices of the animation to play between */
        int firstTileIndex = 0;
        int lastTileIndex = mTiledTextureRegion.getTileCount();
        /* Allow the animation to continuously loop? */
        boolean loopAnimation = true;
        /* Animate the sprite with the data as set defined above */
        animatedSprite.animate(100);
        
        mScene.attachChild(animatedSprite);
    // Notify the callback that we're finished creating the scene, returning
    // mScene to the mEngine object (handled automatically)
    pOnCreateSceneCallback.onCreateSceneFinished(mScene);
		
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback){

	pOnPopulateSceneCallback.onPopulateSceneFinished();
	}


}