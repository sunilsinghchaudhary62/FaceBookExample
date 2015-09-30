package com.example.facebookexample;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;


/*sunil Singh Chaudhary*/


public class Utils {
	private static PackageInfo info;
	static String  something;
	public static String getSHA_key(Context ctx) {

		try {

			info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_SIGNATURES);

			for (Signature signature : info.signatures) {
				MessageDigest md;
				md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				 something = new String(Base64.encode(md.digest(), 0));
				Log.e("Hash key", something);
				System.out.println("Hash key" + something);
			}

		} catch (NameNotFoundException e1) {
			Log.e("name not found", e1.toString());
		} catch (NoSuchAlgorithmException e) {
			Log.e("no such an algorithm", e.toString());
		} catch (Exception e) {
			Log.e("exception", e.toString());
		}
		return something;

	}
}
