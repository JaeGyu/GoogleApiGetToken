package me.jaegyu.token.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

@Service
public class GoogleApiService {

	public String getAccessToken(String code, String tokenUrl, String redirectUrl, String clientId, String clientSecret)
			throws Exception {

		String result = null;

		URI uri = new URI(tokenUrl);

		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(uri);

		final List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("redirect_uri", redirectUrl));
		params.add(new BasicNameValuePair("client_id", clientId));
		params.add(new BasicNameValuePair("client_secret", clientSecret));
		params.add(new BasicNameValuePair("grant_type", "authorization_code"));

		httpPost.setEntity(new UrlEncodedFormEntity(params));
		final HttpResponse response = client.execute(httpPost);

		final HttpEntity he = response.getEntity();

		if (he != null) {
			result = EntityUtils.toString(he);
			System.out.print(result);
			return result;
		}
		
		result = result.replace(", ", "\n");

		return result;
	}

}
