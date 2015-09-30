package com.example.facebookexample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;

import com.facebook.login.LoginResult;


/*sunil Singh Chaudhary*/

public class MainActivity extends Activity {
	private CallbackManager mCallbackManager;
	private Button Login, Email, album, FrndList;
	private List<String> permissionNeeds = Arrays.asList("public_profile",
			"email", "user_posts", "user_photos", "user_birthday",
			"user_friends", "read_custom_friendlists");
	private ArrayList<String> AlbumId_list = new ArrayList<String>();
	private ArrayList<String> Photo_list_id = new ArrayList<String>();
	private GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		Login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				onFblogin();
				Utils.getSHA_key(MainActivity.this);

			}
		});
		album.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getAlbumPics();

			}
		});
		Email.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Emaill();

			}
		});
		FrndList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				GraphRequest request = GraphRequest.newMeRequest(
						AccessToken.getCurrentAccessToken(),
						new GraphRequest.GraphJSONObjectCallback() {
							@Override
							public void onCompleted(JSONObject object,
									GraphResponse response) {
								JSONObject newresponse, totlfrndcount;
								try {
									newresponse = object
											.getJSONObject("friends");
									Log.e("array", newresponse + "");
									JSONArray array = newresponse
											.getJSONArray("data");
									Log.e("array", array + "");
									for (int i = 0; i < array.length(); i++) {
										JSONObject res = array.getJSONObject(i);
										Log.e("name frnd",
												res.getString("name"));
										Log.e("id frnd", res.getString("id"));

									}
									totlfrndcount = newresponse
											.getJSONObject("summary");
									Log.e("Total fb frnds", totlfrndcount
											.getString("total_count"));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						});

				Bundle parameters = new Bundle();
				parameters.putString("fields", "id,friends,name");
				request.setParameters(parameters);
				request.executeAsync();

			}
		});
	}

	private void init() {
		Login = (Button) findViewById(R.id.Login);
		Email = (Button) findViewById(R.id.Email);
		album = (Button) findViewById(R.id.album);
		FrndList = (Button) findViewById(R.id.Frndlist);
		gridView = (GridView) findViewById(R.id.gridView1);
		FacebookSdk.sdkInitialize(this.getApplicationContext());

	}

	protected void getAlbumPics() {

		GraphRequest request = GraphRequest.newMeRequest(
				AccessToken.getCurrentAccessToken(),
				new GraphRequest.GraphJSONObjectCallback() {
					@Override
					public void onCompleted(JSONObject object,
							GraphResponse response) {
						try { // Application code
							JSONObject albums = new JSONObject(object
									.getString("albums"));

							JSONArray data_array = albums.getJSONArray("data");

							for (int i = 0; i < data_array.length(); i++) {
								JSONObject _pubKey = data_array
										.getJSONObject(i);
								String arrayfinal = _pubKey.getString("id");
								Log.d("FB ALbum ID ==  ", "" + arrayfinal);
								AlbumId_list.add(arrayfinal);

							}
							getAlbum_picture(AlbumId_list); // /getting picsssss
						} catch (JSONException E) {
							E.printStackTrace();
						}

					}

				});
		Bundle parameters = new Bundle();
		parameters.putString("fields",
				"id,name,email,gender, birthday, friends,albums");
		request.setParameters(parameters);
		request.executeAsync();

	}

	private void getAlbum_picture(ArrayList<String> Album_id_list) {

		GraphRequest request = GraphRequest.newGraphPathRequest(
				AccessToken.getCurrentAccessToken(), "/" + Album_id_list.get(0)
						+ "/photos/", new GraphRequest.Callback() {
					@Override
					public void onCompleted(GraphResponse response) {
						JSONObject object = response.getJSONObject();
						try {
							JSONArray data_array1 = object.getJSONArray("data");
							for (int i = 0; i < data_array1.length(); i++) {
								JSONObject _pubKey = data_array1
										.getJSONObject(i);
								String arrayfinal = _pubKey.getString("id");
								String picFinals = _pubKey.getString("picture");
								Log.d("pics id == ", "" + arrayfinal);
								Photo_list_id.add(picFinals);

							}
							DetailAdapter adapter = new DetailAdapter(
									MainActivity.this, R.layout.grid_items,
									Photo_list_id);
							gridView.setAdapter(adapter);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

		Bundle parameters = new Bundle();
		parameters.putString("fields", "id,picture");
		parameters.putString("limit", "100");
		request.setParameters(parameters);
		request.executeAsync();

	}


	// Private method to handle Facebook login and callback
	private void onFblogin() {
		mCallbackManager = CallbackManager.Factory.create();

		// Set permissions
		LoginManager.getInstance().logInWithReadPermissions(this,
				permissionNeeds);

		LoginManager.getInstance().registerCallback(mCallbackManager,
				new FacebookCallback<LoginResult>() {

					@Override
					public void onSuccess(final LoginResult loginResult) {

						System.out.println("Success");
						GraphRequest.newMeRequest(loginResult.getAccessToken(),
								new GraphRequest.GraphJSONObjectCallback() {
									@Override
									public void onCompleted(JSONObject json,
											GraphResponse response) {
									
										if (response.getError() != null) {
											// handle error
											System.out.println("ERROR");
										} else {
											System.out.println("Success");
											try {

												String jsonresult = String
														.valueOf(json);
												Log.e("Login Data", jsonresult);
												Log.e("loginResult 1",
														loginResult + "");

											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}

								}).executeAsync();

					}

					@Override
					public void onCancel() {
						Log.d("cancel", "On cancel");
					}

					@Override
					public void onError(FacebookException error) {
						Log.d("error login-", error.toString());
					}
				});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		mCallbackManager.onActivityResult(requestCode, resultCode, data);
	}

	public void Emaill() {

		GraphRequest request = GraphRequest.newMeRequest(
				AccessToken.getCurrentAccessToken(),
				new GraphRequest.GraphJSONObjectCallback() {
					@Override
					public void onCompleted(JSONObject jsonObject,
							GraphResponse response) {

						Log.d("Email response ==", "onCompleted response: " + response);

					}
				});
		Bundle parameters = new Bundle();
		parameters.putString("fields", "id,name,email");
		request.setParameters(parameters);
		request.executeAsync();

	}
}