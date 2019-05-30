BaseChainOfResponsibility   为基础责任划分接口，当满足accept条件时，执行execute方法
BaseChainExecutor   是基础执行器，使用addHandler方法装载对应的责任情况，使用process进行处理
ParameterExecutor   是自定义的一个执行器，它继承自BaseChainExecutor，用于参数校验。

			ParameterExecutor内含有5个内部类，分别对应了参数类型的5种情况。在使用createExecutor方法创建执行器时，会预先装载进这5种情况

Test   加载调用可参考这个测试方法