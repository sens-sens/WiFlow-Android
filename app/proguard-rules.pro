# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class org.apache.** { *; }

-dontwarn javax.security.sasl.AuthenticationException
-dontwarn javax.security.sasl.SaslException
-dontwarn org.ietf.jgss.GSSContext
-dontwarn org.ietf.jgss.GSSCredential
-dontwarn org.ietf.jgss.GSSException
-dontwarn org.ietf.jgss.GSSManager
-dontwarn org.ietf.jgss.GSSName
-dontwarn org.ietf.jgss.Oid
-dontwarn org.springframework.beans.factory.FactoryBean
-dontwarn org.springframework.beans.factory.config.BeanDefinition
-dontwarn org.springframework.beans.factory.config.BeanDefinitionHolder
-dontwarn org.springframework.beans.factory.support.AbstractBeanDefinition
-dontwarn org.springframework.beans.factory.support.BeanDefinitionBuilder
-dontwarn org.springframework.beans.factory.support.BeanDefinitionRegistry
-dontwarn org.springframework.beans.factory.support.ManagedMap
-dontwarn org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
-dontwarn org.springframework.beans.factory.xml.BeanDefinitionParser
-dontwarn org.springframework.beans.factory.xml.BeanDefinitionParserDelegate
-dontwarn org.springframework.beans.factory.xml.NamespaceHandlerSupport
-dontwarn org.springframework.beans.factory.xml.ParserContext
-dontwarn org.springframework.beans.factory.xml.XmlReaderContext
-dontwarn org.springframework.context.support.FileSystemXmlApplicationContext
-dontwarn org.springframework.util.StringUtils
-dontwarn org.springframework.util.xml.DomUtils


# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable
-dontwarn org.slf4j.impl.StaticLoggerBinder

-dontwarn org.slf4j.impl.StaticMDCBinder

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile