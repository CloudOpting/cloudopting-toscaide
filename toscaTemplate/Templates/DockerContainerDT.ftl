FROM <#if image?has_content>${image}</#if>
MAINTAINER \"Luca Gioppo\" <gioppo@csi.it>

# PREPARE PUPPET ENVIRONMENT
ADD puppet/modules/ /etc/puppet/modules/
ADD <#if servicePath?has_content>${servicePath}</#if>/<#if puppetFile?has_content>${puppetFile}</#if> /etc/puppet/manifests/

# EXECUTE PUPPET STANDALONE
RUN puppet apply /etc/puppet/manifests/<#if puppetFile?has_content>${puppetFile}</#if> --verbose --detailed-exitcodes || [ $? -eq 2 ]

# OPEN UP PORT
<#if exposedPorts?has_content>EXPOSE ${exposedPorts?join(" ")}</#if>

# SET ENTRYPOINT AND CMD
<#if entrypoint?has_content>ENTRYPOINT ${entrypoint}</#if>
CMD <#if cmd?has_content>${cmd}</#if>