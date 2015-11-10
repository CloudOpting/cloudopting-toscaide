class{'solr':
<#if url?has_content>url => '${url}',</#if>
<#if version?has_content>version => '${version}',</#if>
<#if solr_host?has_content>dbhost => '${solr_host}',</#if>
<#if solr_port?has_content>dbname => ${solr_port},</#if>
}

