/* Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.snaker.designer.config;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 单态实例操作model.xml配置文件
 * @author yuqs
 * @version 1.0
 */
public class Configuration {
	private static Configuration configuration = new Configuration();

	private static final String MODEL_PROCESS = "model-process.xml";
	private static final String MODEL_FORM = "model-form.xml";

	private List<Component> components = new ArrayList<Component>();

	private Configuration() {
		load(MODEL_PROCESS);
		load(MODEL_FORM);
	}

	public static Configuration getInstance() {
		return configuration;
	}

	/**
	 * 组装对象Component
	 */
	@SuppressWarnings("unchecked")
	public void load(String fileName) {
		try {
	        URL url = null;
	        
	        ClassLoader threadContextClassLoader = Thread.currentThread().getContextClassLoader();
	        if (threadContextClassLoader != null) {
	            url = threadContextClassLoader.getResource(fileName);
	        }
			SAXReader reader = new SAXReader();
			Document document = reader.read(url);
			Element root = document.getRootElement();
			Iterator<Element> iter = root.elementIterator("component");
			while (iter.hasNext()) {
				Element comp = (Element) iter.next();
				Component component = new Component();
				component.setName(comp.attributeValue("name"));
				component.setDisplayName(comp.attributeValue("displayName"));
				component.setDescript(comp.attributeValue("descript"));
				component.setGroup(comp.attributeValue("group"));
				component.setType(comp.attributeValue("type"));
				component.setVisible(comp.attributeValue("visible"));
				component.setIconSmall(comp.attributeValue("iconSmall"));
				component.setIconLarge(comp.attributeValue("iconLarge"));
				component.setClazz(comp.attributeValue("class"));

				Iterator<Element> itattrs = comp.elementIterator("property");
				while (itattrs.hasNext()) {
					Element ep = (Element) itattrs.next();
					Property prop = new Property();
					prop.setName(ep.attributeValue("name"));
					prop.setType(ep.attributeValue("type"));
					prop.setDefaultValue(ep.attributeValue("defaultValue"));
					prop.setValidator(ep.attributeValue("validator"));
					prop.setValues(ep.attributeValue("values"));
					prop.setVisible(ep.attributeValue("visible"));
					prop.setGroup(ep.attributeValue("group"));

					component.getProperties().add(prop);
				}
				components.add(component);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Component> getComponents() {
		return components;
	}
}
