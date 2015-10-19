package eu.cloudopting.tosca;

import java.io.InputStream;

import javax.xml.bind.JAXBException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.persistence.dynamic.DynamicEntity;
import org.eclipse.persistence.dynamic.DynamicType;
import org.eclipse.persistence.exceptions.DynamicException;
import org.eclipse.persistence.jaxb.dynamic.DynamicJAXBContext;
import org.eclipse.persistence.jaxb.dynamic.DynamicJAXBContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;



public class IdeManager {
	private static final Logger log = LoggerFactory
			.getLogger(IdeManager.class);
	
	@Autowired
	ToscaService toscaService;

	public void readXsd() {
		String PATH_TO_XSD = null;
		InputStream in;
		DynamicJAXBContext jaxbContext = null;
		DynamicType rootType = null;
		DynamicEntity root = null;
		try {
			in = new FileInputStream(PATH_TO_XSD);
		
		jaxbContext = DynamicJAXBContextFactory
				.createContextFromXSD(in, null, Thread.currentThread()
						.getContextClassLoader(), null);
		String YOUR_ROOT_TYPE = null;
		rootType = jaxbContext.getDynamicType(YOUR_ROOT_TYPE);
		root = rootType.newDynamicEntity();
		traverseProps(jaxbContext, root, rootType, 0);
		} catch (FileNotFoundException | JAXBException | DynamicException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void traverseProps(DynamicJAXBContext c, DynamicEntity e,
			DynamicType t, int level) throws DynamicException,
			InstantiationException, IllegalAccessException {
		if (t != null) {
			log.info(level + "type [" + t.getName() + "] of class ["
					+ t.getClassName() + "] has " + t.getNumberOfProperties()
					+ " props");
			for (String pName : t.getPropertiesNames()) {
				Class<?> clazz = t.getPropertyType(pName);
				log.info(level + "prop [" + pName + "] in type: "
						+ clazz);
				// logger.info("prop [" + pName + "] in entity: " +
				// e.get(pName));

				if (clazz == null) {
					// need to create an instance of object
					String updatedClassName = pName.substring(0, 1)
							.toUpperCase() + pName.substring(1);
					log.info(level
							+ "Creating new type instance for " + pName
							+ " using following class name: "
							+ updatedClassName);
					DynamicType child = c.getDynamicType("generated."
							+ updatedClassName);
					DynamicEntity childEntity = child.newDynamicEntity();
					e.set(pName, childEntity);
					traverseProps(c, childEntity, child, level + 1);
				} else {
					// just set empty value
					e.set(pName, clazz.newInstance());
				}
			}
		} else {
			log.warn("type is null");
		}
	}
}
