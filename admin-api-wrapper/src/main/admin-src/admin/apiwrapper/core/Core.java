package admin.apiwrapper.core;



import com.example.pingfedadmin.invoker.ApiClient;

import admin.MyClientHttpRequestInterceptor;
import admin.TransformBean;

public class Core {
	
	private ApiClient apiClient;
	private MyClientHttpRequestInterceptor interceptor;
	public void setResponseTransformBeans(TransformBean... transformBeans) {
		this.interceptor.setResponseTransformBeans(transformBeans);
	}
	public void setRequestTransformBeans(TransformBean... transformBeans) {
		this.interceptor.setRequestTransformBeans(transformBeans);
	}
	public Core(ApiClient apiClient, MyClientHttpRequestInterceptor interceptor) {
		super();
		this.apiClient = apiClient;
		this.interceptor = interceptor;
	}
	public ApiClient getApiClient() {
		return apiClient;
	}
	public void setApiClient(ApiClient apiClient) {
		this.apiClient = apiClient;
	}
	
	public void clearTransformers()
	{
		setRequestTransformBeans(null);
		setResponseTransformBeans(null);
	}

}
