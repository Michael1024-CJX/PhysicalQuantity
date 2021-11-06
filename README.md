# PhysicalQuantity
Modeling physical quantities

为物理量建模，使得物理量能够运算，单位转换。

## 使用方式
可以通过查看 JUnit 编写的 test 了解的更多。

创建 `UnitFactory`, 默认提供了Yaml文档定义的Unit以及unit之间的比率
```java
File config = new File("src/test/resources/unit/");
UnitRegister unitRegister = new YAMLUnitRegister(config);
UnitFactory factory = new DefaultUnitFactory(unitRegister);
```
通过`UnitFactory` 创建`QuanityFactory`.
```java
QuantityFactory quantityFactory = new QuantityFactory(unitFactory);
```
```java
// 通过 `QuantityFactory` 创建物理量。
PhysicalQuantity height = quantityFactory.of(180, "cm");

// 物理量能够转换单位
PhysicalQuantity heightByM = height.convertTo("m");

// 物理量能够运算，加减乘数
PhysicalQuantity multiply = height.multiply(2);

// 物理量能够比较
int compare = multiply.compareTo(height);
```

## 单位转换

底层的数据结构是一个双向链表，链表的节点存储的具体的单位，以及该单位与下一个节点的比值。这样在物理量的单位的时候，只需要获取原单位与目标单位，即可知道两个单位之间的节点，通过运算即可获取单位的转换率。

存储转换率时，不必为任意两个单位设置转换率，只需要这两个单位能够通过其他单位实现关联即可。
