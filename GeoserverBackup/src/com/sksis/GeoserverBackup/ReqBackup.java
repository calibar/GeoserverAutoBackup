package com.sksis.GeoserverBackup;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class ReqBackup {
	public void SendBackupPost(String url, String path,String username,String password) {
		//set basic authentication
		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
		provider.setCredentials(AuthScope.ANY, credentials);
		CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
		try {
			//configure request
			HttpPost req=new HttpPost(url);
			String json="{" + 
					"   \"backup\":{" + 
					"      \"archiveFile\":\""+path+"\"," + 
					"      \"overwrite\":true," + 
					"      \"options\":{" +" \"option\": [\"BK_BEST_EFFORT=true\"]"+
					"      }" + 
					"   }" + 
					"}";
			System.out.println(json);
			StringEntity body=new StringEntity(json);
			req.setEntity(body);
			req.setHeader("Content-Type","application/json");
			//handle response
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            String responseBody =client.execute(req, responseHandler);
            System.out.println(responseBody);
            client.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}
	public void Setinterval(String url,String username,String password,String pathfolder,int interval) throws InterruptedException {
		for (int i = 0; i < 15; i++) {
			if (i!=14) {
				String filename="RegularBackup"+Integer.toString(i+1)+".zip";
				String path=pathfolder+"/regular/"+filename;
				System.out.println(path);
				SendBackupPost(url,path, username, password);
			}else {
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				String filename="Backup"+dateFormat.format(date)+".zip";
				String path= pathfolder+"/monthly/"+filename;
				System.out.println(path);
				SendBackupPost(url, path, username, password);
			}
			for (int j = 0; j < interval; j++) {
				int day=24*60*60*1000;
				Thread.sleep(day);
			}
		}
	}
}
