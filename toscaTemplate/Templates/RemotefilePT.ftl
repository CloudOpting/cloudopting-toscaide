include wget

wget::fetch {<#if source?has_content>'${source}':</#if> 
<#if destination?has_content>destination => '${destination}',</#if>
<#if co_jack_user?has_content>user => '${co_jack_user}',</#if>
<#if co_jack_password?has_content>password => '${co_jack_password}',</#if>
<#if require?has_content>require => ${require},</#if>
<#if before?has_content>before => ${before},</#if>
}<#if unzip>->
archive::extract{<#if source?has_content>'${archive}':</#if>
<#if target?has_content>target => '${target}',</#if>
<#if destination?has_content>src_target => '${destination}',</#if>
<#if extension?has_content>extension => '${extension}',</#if>
<#if archive_user?has_content>user => '${archive_user}',</#if>
<#if root_dir?has_content>root_dir => ${root_dir},</#if>
}
</#if>
