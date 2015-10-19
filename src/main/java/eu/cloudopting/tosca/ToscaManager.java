package eu.cloudopting.tosca;

import java.io.InputStream;

public class ToscaManager {

	public void readXsd() {
		InputStream in = new FileInputStream(PATH_TO_XSD);
		DynamicJAXBContext jaxbContext = DynamicJAXBContextFactory
				.createContextFromXSD(in, null, Thread.currentThread()
						.getContextClassLoader(), null);
		DynamicType rootType = jaxbContext.getDynamicType(YOUR_ROOT_TYPE);
		DynamicEntity root = rootType.newDynamicEntity();
		traverseProps(jaxbContext, root, rootType, 0);
	}

	private void traverseProps(DynamicJAXBContext c, DynamicEntity e,
			DynamicType t, int level) throws DynamicException,
			InstantiationException, IllegalAccessException {
		if (t != null) {
			logger.info(indent(level) + "type [" + t.getName() + "] of class ["
					+ t.getClassName() + "] has " + t.getNumberOfProperties()
					+ " props");
			for (String pName : t.getPropertiesNames()) {
				Class<?> clazz = t.getPropertyType(pName);
				logger.info(indent(level) + "prop [" + pName + "] in type: "
						+ clazz);
				// logger.info("prop [" + pName + "] in entity: " +
				// e.get(pName));

				if (clazz == null) {
					// need to create an instance of object
					String updatedClassName = pName.substring(0, 1)
							.toUpperCase() + pName.substring(1);
					logger.info(indent(level)
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
			logger.warn("type is null");
		}
	}
}
