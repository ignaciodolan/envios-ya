/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enviosya.login.beans;

/**
 *
 * @author Ruso
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

public class FbGraph {
	private String accessToken;

	public FbGraph(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getFBGraph() {
		String graph = null;
		try {

			String g = "https://graph.facebook.com/me?" + accessToken;
			URL u = new URL(g);
			URLConnection c = u.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					c.getInputStream()));
			String inputLine;
			StringBuffer b = new StringBuffer();
			while ((inputLine = in.readLine()) != null)
				b.append(inputLine + "\n");
			in.close();
			graph = b.toString();
			System.out.println(graph);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("ERROR in getting FB graph data. " + e);
		}
		return graph;
	}

	public Map getGraphData(String fbGraph) {
		Map fbProfile = new HashMap();
		try {
			JsonObject json = new JsonObject();
                        json.add(fbGraph, json);
			fbProfile.put("id", json.get("id"));
			fbProfile.put("first_name", json.get("first_name"));
			if (json.has("email"))
				fbProfile.put("email", json.get("email"));
			if (json.has("gender"))
				fbProfile.put("gender", json.get("gender"));
		} catch (JsonIOException e) {
			e.printStackTrace();
			throw new RuntimeException("ERROR in parsing FB graph data. " + e);
		}
		return fbProfile;
	}
}