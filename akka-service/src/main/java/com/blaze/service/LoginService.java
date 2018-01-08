package com.blaze.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.blaze.model.ErrorResponse;
import com.blaze.model.Login;
import com.blaze.model.Response;
import com.blaze.model.SessionResponse;
import com.blaze.model.UserServiceMessage;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Source;
import akka.util.ByteString;

public class LoginService extends UntypedActor {

	final ActorSystem system = ActorSystem.create();
	final ActorMaterializer materializer = ActorMaterializer.create(system);

	public static Props props() {
		return Props.create(LoginService.class, () -> new LoginService());
	}

	public void don() {
		// final CompletionStage<HttpResponse> responseFuture = Http.get(system)
		// .singleRequest(HttpRequest.create("http://api.fixer.io/latest"),
		// materializer);
		HttpRequest req = HttpRequest.create("http://localhost:8000/login/name/user");
		// HttpRequest request = new HttpRequest(HttpMethod.GET,
		// "http://api.fixer.io/latest",
		// null,
		// Map.class,
		// null) {};
		final CompletionStage<HttpResponse> completionStage = Http.get(system)
				.singleRequest(HttpRequest.GET("http://localhost:8000/login/name/user"), materializer);
		// .singleRequest(HttpRequest.GET("http://api.fixer.io/latest"), materializer);
		// completionStage.thenApply(s->{
		// s.getClass();
		// });

		CompletableFuture<HttpResponse> future = completionStage.toCompletableFuture();
		try {

			HttpResponse response = future.get();
			// System.out.println(response.entity().getDataBytes());

			// response.

			// System.out.println(future.join().);
			System.out.println(response);
			Source<ByteString, Object> source = response.entity().getDataBytes();
			System.out.println("-----------------------");
			System.out.println(source.log("name"));
			System.out.println("-----------------------");
			System.out.println(response.entity());

			future.whenComplete((t, ex) -> {
				if (t != null) {
					System.out.println("t :O");
				} else {
					System.out.println("fail");
				}
			});

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String createToken(String user, String password) {
		String token = null;
		Algorithm algorithm;
		try {
			algorithm = Algorithm.HMAC256("secret");
			token = JWT.create().withIssuer("auth0").withIssuedAt(new Date()).withExpiresAt(new Date())
					.withClaim("user", user).withClaim("password", password).sign(algorithm);
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("error :(");
			System.out.println(e);
		}
		return token;
	}

	private final String LOGIN_URL = "http://localhost:8000/login/";

	public Response don2(String userName, String password) {
		Response result = null;
		RestTemplate restTemplate = new RestTemplate();
		Map<Object, Object> returnResponse = null;
		HttpHeaders tokenHeader = new HttpHeaders();
		tokenHeader.add("User-Agent", "curl/7.51.0");
		tokenHeader.add("Accept", "application/json");
		HttpEntity<Map> request = new HttpEntity<>(tokenHeader);
		ResponseEntity<Map> response;
			response = restTemplate.exchange(LOGIN_URL + userName + "/" + password, HttpMethod.GET, request, Map.class);
			returnResponse = response.getBody();
			if (returnResponse != null) {
				if (returnResponse.get("objectId") != null) {
					// String objectId=(String) returnResponse.get("objectId");
					
					String firstName = (String) returnResponse.get("first_name");
					String lastName = (String) returnResponse.get("last_name");
					String email = (String) returnResponse.get("email");
					password = (String) returnResponse.get("password");
					String token = createToken(email, password);
					SessionResponse session = new SessionResponse();
					session.setToken(token);
					session.setCode(200);
					result = session;
				} else {
					ErrorResponse error = new ErrorResponse();
					error.setCode(101);
					error.setMessage("Not user");
					result = error;
				}
			} else {
				ErrorResponse error = new ErrorResponse();
				error.setCode(100);
				error.setMessage("Not user");
				result = error;
			}
		return result;
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof Login) {
			Login login = (Login) message;
			String user = login.getUserName();
			String password = login.getPassword();
			try {
			Response response = don2(user, password);
			sender().tell(response, sender());
			}catch(Exception e) {
				sender().tell(e, sender());
			}
		} else {
			System.out.println("not recognized");
		}

	}

}
