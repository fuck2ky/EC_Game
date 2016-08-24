@ echo off 

protogen -i:element_globalmsg.proto 		-o:../Client/SowrdCard/Assets/Script/NetWork/Protobuf/element_globalmsg.cs

protogen -i:element_simple.proto 			-o:../Client/SowrdCard/Assets/Script/NetWork/Protobuf/element_simple.cs

protogen -i:element_login.proto				-o:../Client/SowrdCard/Assets/Script/NetWork/Protobuf/element_login.cs

echo 客户端			buffer生成成功

protoc --java_out=../Server/elementproject/gmserver/src/main/java   ./element_login.proto

protoc --java_out=../Server/elementproject/gmserver/src/main/java	./element_simple.proto

protoc --java_out=../Server/elementproject/gmserver/src/main/java	./element_globalmsg.proto

echo Logic服务器		buffer生成成功

protoc --java_out=../Server/elementproject/gtserver/src/main/java   ./element_login.proto

protoc --java_out=../Server/elementproject/gtserver/src/main/java	./element_simple.proto

protoc --java_out=../Server/elementproject/gtserver/src/main/java	./element_globalmsg.proto

echo Game服务器		buffer生成成功


pause