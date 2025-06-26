package org.citi;

import com.jfinal.template.Directive;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.citi.annotation.*;
import org.citi.constant.Constants;
import org.citi.directive.CompactDirective;
import org.citi.directive.DSLDirective;
import org.citi.directive.NamespaceDirective;
import org.citi.directive.TripDirective;
import org.citi.executor.*;
import org.citi.filter.directive.FilterDirective;
import org.citi.filter.directive.SortDirective;
import org.citi.metadata.MethodMetadata;
import org.citi.metadata.NamespaceNode;
import org.citi.parser.*;
import org.citi.result.OriginalResult;
import org.citi.result.Pageable;
import org.citi.utils.ShareUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Carson
 * @created 2025/6/12 星期四 上午 11:33
 */
@Slf4j
public class ESMapperDSLParser {
    private final Engine engine = Engine.create(Constants.EngineConstants.ENGINE);
    private final Map<String, NamespaceNode> namespaceNodeMap;
    private final Map<Method, MapperMethodExecutor> mapperMethodExecutorMap;
    private ApplicationContext applicationContext;

    private ESMapperDSLParser() {
        this.engine.setToClassPathSourceFactory();
        this.engine.addDirective("namespace", NamespaceDirective.class);
        this.engine.addDirective("dsl", DSLDirective.class);
        // other directives
        this.engine.addDirective("compact", CompactDirective.class);
        this.engine.addDirective("trip", TripDirective.class);
        this.engine.addDirective("filters", FilterDirective.class);
        this.engine.addDirective("sorts", SortDirective.class);

        //share methods
        this.engine.addSharedMethod(ShareUtils.class);
        this.engine.addSharedMethod(StringUtils.class);
        //default settings
        this.engine.setDevMode(false);
        this.engine.setDatePattern("yyyy-MM-dd HH:mm:ss");
        this.namespaceNodeMap = new HashMap<String, NamespaceNode>();
        this.mapperMethodExecutorMap = new HashMap<>();
    }

    public void addNamespaceNode(NamespaceNode namespaceNode) {
        if (this.namespaceNodeMap.containsKey(namespaceNode.getNamespace())){
            this.namespaceNodeMap.get(namespaceNode.getNamespace()).merge(namespaceNode);
        }else {
            this.namespaceNodeMap.put(namespaceNode.getNamespace(), namespaceNode);
        }
    }

    public void loadDSLFile(String path, ResourceLoader resourceLoader){
        try{
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(resourceLoader);
            Resource[] resources = resolver.getResources(path);
            for(Resource resource : resources){
                log.info("start load {}", resource.getFilename());
                InputStream inputStream = resource.getInputStream();
                try{
                    String content = FileCopyUtils.copyToString(new InputStreamReader(inputStream));
                    content = content.replaceAll("\r\n", "\n");
                    if (content.indexOf("\r") > 0){
                        content = content.replaceAll("\r", "\n");
                    }
                    content = content.replace("\n", "\r\n");
                    getInstance().getEngine().getTemplateByString(content).renderToString();
                } catch (Exception e) {
                    try {
                        inputStream.close();
                    } catch (Exception e1) {
                        e.addSuppressed(e1);
                    }
                    throw e;
                }
                inputStream.close();
                log.info("end load {}", resource.getFilename());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, NamespaceNode> getNamespaceNodeMap() {
        return namespaceNodeMap;
    }

    public Map<Method, MapperMethodExecutor> getMapperMethodExecutorMap() {
        return mapperMethodExecutorMap;
    }

    public Engine getEngine() {
        return engine;
    }

    public void addDirective(String command, Class<? extends Directive> directiveClass) {
        this.engine.addDirective(command, directiveClass);
    }

    public void addMapper(Class<?> clazz){
        ESMapper esMapper = clazz.getAnnotation(ESMapper.class);
        String namespace = clazz.getName();
        String defaultIndex = esMapper.defaultIndex();
        if (StringUtils.isNotBlank(esMapper.namespace())){
            namespace = esMapper.namespace();
        }

        // namespace node are resolved by "namespace" directive
        NamespaceNode namespaceNode = namespaceNodeMap.get(namespace);
        Method[] methods = clazz.getMethods();
        for(Method method : methods){
            MethodMetadata methodMetadata = this.resolveMethod(method);
            methodMetadata.setDefaultIndex(defaultIndex);
            resolveExecutor(methodMetadata, namespaceNode);
        }
    }

    private Template getNamespaceTemplate(NamespaceNode namespaceNode, String id) {
        return namespaceNode.getDslNodeMap().get(id).getTemplate();
    }

    private void resolveNonBulkExecutor(MethodMetadata methodMetadata, NamespaceNode namespaceNode){
        String mapperClazzName = methodMetadata.getMethod().getDeclaringClass().getName();

        Method method = methodMetadata.getMethod();
        Get get  = method.getAnnotation(Get.class);
        if (null != get){
            if (null == namespaceNode) throw new RuntimeException("Not found DSL file for ESMapper " + mapperClazzName);
            methodMetadata.setId(get.value());
            if (StringUtils.isNotBlank(get.index())){
                methodMetadata.setDefaultIndex(get.index());
            }
            this.mapperMethodExecutorMap.put(method, new MapperGetExecutor(getNamespaceTemplate(namespaceNode, methodMetadata.getId()), methodMetadata));
        } else {
            MGet mGet = method.getAnnotation(MGet.class);
            if (null != mGet){
                if (null == namespaceNode) throw new RuntimeException("Not found DSL file for ESMapper " + mapperClazzName);
                methodMetadata.setId(mGet.value());
                this.mapperMethodExecutorMap.put(method, new MapperGetExecutor(getNamespaceTemplate(namespaceNode, methodMetadata.getId()), methodMetadata));
            } else {
                Put put  = method.getAnnotation(Put.class);
                if (null != put){
                    methodMetadata.setId(put.value());
                    methodMetadata.setUrlParameters(put.urlParameters());
                    this.mapperMethodExecutorMap.put(method, new MapperPutExecutor(methodMetadata));
                } else {
                    Post post = method.getAnnotation(Post.class);
                    if (null != post){
                        methodMetadata.setId(post.value());
                        methodMetadata.setUrlParameters(post.urlParameters());
                        this.mapperMethodExecutorMap.put(method, new MapperPostExecutor(methodMetadata));
                    } else {
                        DeleteByQuery deleteByQuery = method.getAnnotation(DeleteByQuery.class);
                        if (null != deleteByQuery){
                            methodMetadata.setId(deleteByQuery.value());
                            methodMetadata.setUrlParameters(deleteByQuery.urlParameters());
                            this.mapperMethodExecutorMap.put(method, new MapperDeleteByQueryExecutor(getNamespaceTemplate(namespaceNode, methodMetadata.getId()), methodMetadata));
                        } else {
                            Custom custom = method.getAnnotation(Custom.class);
                            if (null != custom){
                                methodMetadata.setId(custom.value());
                                methodMetadata.setDefaultIndex(custom.index());
                                this.mapperMethodExecutorMap.put(method, new CustomMapperExecutor(custom, getNamespaceTemplate(namespaceNode, custom.value()), methodMetadata));
                            }
                        }
                    }
                }
            }
        }
    }

    private void resolveExecutor(MethodMetadata methodMetadata, NamespaceNode namespaceNode){
        Method method = methodMetadata.getMethod();
        Bulk bulk = method.getAnnotation(Bulk.class);
        if (null != bulk){
            methodMetadata.setId(bulk.value());
            methodMetadata.setUrlParameters(bulk.urlParameters());
            this.mapperMethodExecutorMap.put(method, new MapperBulkExecutor(methodMetadata));
        }else{
          resolveNonBulkExecutor(methodMetadata, namespaceNode);
        }
    }

    private void resolveMethodParams(Method method, MethodMetadata methodMetadata){
        List<String> paramsName = new ArrayList<>();
        int index = 0;
        for(Parameter parameter : method.getParameters()){
            String paramName = parameter.getName();
            Param param = parameter.getAnnotation(Param.class);
            if (null != param){
                paramName = param.name();
            }
            paramsName.add(paramName);

            Id id = parameter.getAnnotation(Id.class);
            if (null != id){
                methodMetadata.setIdParamIndex(index);
            }
            index++;
        }
        methodMetadata.setParamsName(paramsName);
    }

    private void resolveMethodReturnType(Method method, MethodMetadata methodMetadata){
        Type returnType = method.getGenericReturnType();
        if (returnType instanceof ParameterizedType){
            methodMetadata.setReturnType(returnType);
        }
        methodMetadata.setResturnClass(method.getReturnType());
        Deserializer deserializer = method.getAnnotation(Deserializer.class);
        if (null != deserializer){
            String bean = deserializer.bean();
            Class<? extends ResultDeserializer> deserializerClass = deserializer.deserializer();
            methodMetadata.setResultParser(new CustomResultParser(bean, deserializerClass));
        } else if(OriginalResult.class.isAssignableFrom(method.getReturnType())){
            methodMetadata.setResultParser(new OriginalResultParser());
        } else {
            Type genericReturnType = method.getGenericReturnType();
            ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
            if (List.class.isAssignableFrom(method.getReturnType())){
                methodMetadata.setResultParser(new CollectionResultParser());
                methodMetadata.setReturnType(parameterizedType.getActualTypeArguments()[0]);
            } else if(Pageable.class.isAssignableFrom(method.getReturnType())){
                methodMetadata.setResultParser(new PageableResultParser());
                methodMetadata.setReturnType(parameterizedType.getActualTypeArguments()[0]);
            } else {
                methodMetadata.setResultParser(new SingleResultParser());
            }
        }
    }

    private MethodMetadata resolveMethod(Method method){
        MethodMetadata methodMetadata = new MethodMetadata(method);
        resolveMethodParams(method, methodMetadata);
        resolveMethodReturnType(method, methodMetadata);
        return methodMetadata;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static ESMapperDSLParser getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final ESMapperDSLParser INSTANCE = new ESMapperDSLParser();
        private SingletonHolder() {

        }
    }
}
