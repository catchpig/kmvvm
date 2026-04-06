# Detekt 代码规范规则文档

> 本文档由 `detekt.yml` 自动生成，记录了项目使用的所有代码检查规则及其配置。

## 目录

- [基础配置](#基础配置)
- [Comments 注释规则](#comments-注释规则)
- [Complexity 复杂度规则](#complexity-复杂度规则)
- [Coroutines 协程规则](#coroutines-协程规则)
- [Empty Blocks 空块规则](#empty-blocks-空块规则)
- [Exceptions 异常规则](#exceptions-异常规则)
- [Naming 命名规则](#naming-命名规则)
- [Performance 性能规则](#performance-性能规则)
- [Potential Bugs 潜在 Bug 规则](#potential-bugs-潜在-bug-规则)
- [Style 代码风格规则](#style-代码风格规则)

---

## 基础配置

| 配置项 | 值 | 说明 |
|--------|-----|------|
| `maxIssues` | 0 | 最大允许问题数，超过则构建失败 |
| `excludeCorrectable` | false | 是否排除可自动修正的问题 |
| `warningsAsErrors` | false | 是否将所有警告视为错误 |

---

## Comments 注释规则

### 启用的规则

| 规则 | 说明 |
|------|------|
| *无* | 当前无启用的注释规则 |

### 禁用的规则

| 规则 | 说明 |
|------|------|
| `AbsentOrWrongFileLicense` | 文件缺少许可证头 |
| `CommentOverPrivateFunction` | 私有函数上有注释 |
| `CommentOverPrivateProperty` | 私有属性上有注释 |
| `DeprecatedBlockTag` | 使用了废弃的块标签 |
| `EndOfSentenceFormat` | 句末格式不正确 |
| `KDocReferencesNonPublicProperty` | KDoc 引用了非公开属性 |
| `OutdatedDocumentation` | 文档过时 |
| `UndocumentedPublicClass` | 公开类缺少文档 |
| `UndocumentedPublicFunction` | 公开函数缺少文档 |
| `UndocumentedPublicProperty` | 公开属性缺少文档 |

---

## Complexity 复杂度规则

### 启用的规则

| 规则 | 配置 | 说明 |
|------|------|------|
| `ComplexCondition` | threshold: 4 | 条件表达式过于复杂 |
| `LargeClass` | threshold: 600 | 类过长（超过600行） |
| `LongParameterList` | functionThreshold: 6, constructorThreshold: 7, exclude: annotation | 参数列表过长 |
| `StringLiteralDuplication` | threshold: 3 | 字符串字面量重复 |

### 禁用的规则

| 规则 | 说明 |
|------|------|
| `CognitiveComplexMethod` | 方法认知复杂度过高 |
| `ComplexInterface` | 接口过于复杂 |
| `CyclomaticComplexMethod` | 圈复杂度过高 |
| `LabeledExpression` | 标签表达式 |
| `LongMethod` | 方法过长 |
| `MethodOverloading` | 方法重载过多 |
| `NamedArguments` | 命名参数过多 |
| `NestedBlockDepth` | 嵌套块过深 |
| `NestedScopeFunctions` | Scope 函数嵌套过多 |
| `ReplaceSafeCallChainWithRun` | 用 run 替代安全调用链 |
| `TooManyFunctions` | 函数过多 |

---

## Coroutines 协程规则

### 启用的规则

| 规则 | 配置 | 说明 |
|------|------|------|
| `InjectDispatcher` | - | 注入错误的调度器 |
| `RedundantSuspendModifier` | - | 冗余的 suspend 修饰符 |
| `SleepInsteadOfDelay` | - | 使用 Thread.sleep 而不是 delay |
| `SuspendFunWithFlowReturnType` | - | suspend 函数返回 Flow |

### 禁用的规则

| 规则 | 说明 |
|------|------|
| `GlobalCoroutineUsage` | 使用全局协程 |
| `SuspendFunSwallowedCancellation` | 协程作用域中被吞掉的取消异常 |
| `SuspendFunWithCoroutineScopeReceiver` | suspend 函数使用协程作用域作为接收者 |

---

## Empty Blocks 空块规则

### 启用的规则

| 规则 | 配置 | 说明 |
|------|------|------|
| `EmptyCatchBlock` | allowedExceptionNameRegex: '_|(ignore\|expected).*' | 空的 catch 块 |
| `EmptyDefaultConstructor` | - | 空的默认构造函数 |
| `EmptyDoWhileBlock` | - | 空的 do-while 块 |
| `EmptyElseBlock` | - | 空的 else 块 |
| `EmptyFinallyBlock` | - | 空的 finally 块 |
| `EmptyForBlock` | - | 空的 for 块 |
| `EmptyFunctionBlock` | ignoreOverridden: true | 空的函数块 |
| `EmptyIfBlock` | - | 空的 if 块 |
| `EmptyInitBlock` | - | 空的 init 块 |
| `EmptyKtFile` | - | 空的 Kotlin 文件 |
| `EmptySecondaryConstructor` | - | 空的次构造函数 |
| `EmptyTryBlock` | - | 空的 try 块 |
| `EmptyWhenBlock` | - | 空的 when 块 |
| `EmptyWhileBlock` | - | 空的 while 块 |

### 禁用的规则

| 规则 | 说明 |
|------|------|
| `EmptyClassBlock` | 空的类块 |

---

## Exceptions 异常规则

### 启用的规则

| 规则 | 说明 |
|------|------|
| `ExceptionRaisedInUnexpectedLocation` | 在意外位置抛出异常 |
| `InstanceOfCheckForException` | 用 instanceof 检查异常类型 |
| `RethrowCaughtException` | 在 catch 中重新抛出异常 |
| `ReturnFromFinally` | 从 finally 返回 |
| `ThrowingExceptionFromFinally` | 在 finally 中抛出异常 |
| `ThrowingNewInstanceOfSameException` | 抛出相同异常的实例 |
| `TooGenericExceptionThrown` | 抛出太宽泛的异常 |

### 禁用的规则

| 规则 | 说明 |
|------|------|
| `NotImplementedDeclaration` | 使用 NotImplementedDeclaration |
| `ObjectExtendsThrowable` | Object 继承 Throwable |
| `PrintStackTrace` | 打印堆栈跟踪 |
| `SwallowedException` | 吞掉异常 |
| `ThrowingExceptionInMain` | 在 main 函数中抛出异常 |
| `ThrowingExceptionsWithoutMessageOrCause` | 抛出的异常没有消息或原因 |
| `TooGenericExceptionCaught` | catch 了太宽泛的异常 |

---

## Naming 命名规则

### 启用的规则

| 规则 | 配置 | 说明 |
|------|------|------|
| `ClassNaming` | classPattern: '[A-Z][a-zA-Z0-9]*' | 类命名（应使用 PascalCase） |
| `ConstructorParameterNaming` | parameterPattern: '[a-z][A-Za-z0-9]*' | 构造函数参数命名 |
| `EnumNaming` | enumEntryPattern: '[A-Z][_a-zA-Z0-9]*' | 枚举命名 |
| `FunctionNaming` | functionPattern: '[a-z][a-zA-Z0-9]*' | 函数命名（应使用 camelCase） |
| `FunctionParameterNaming` | parameterPattern: '[a-z][A-Za-z0-9]*' | 函数参数命名 |
| `InvalidPackageDeclaration` | rootPackage: '' | 包声明无效 |
| `MatchingDeclarationName` | mustBeFirst: true | 声明名称与文件名匹配 |
| `MemberNameEqualsClassName` | ignoreOverridden: true | 成员名与类名相同 |
| `NoNameShadowing` | - | 禁止名称遮蔽 |
| `ObjectPropertyNaming` | constantPattern: '[A-Za-z][_A-Za-z0-9]*' | 对象属性命名 |
| `PackageNaming` | packagePattern: '[a-z]+(\.[a-z][A-Za-z0-9]*)*' | 包命名（应使用小写字母） |
| `TopLevelPropertyNaming` | constantPattern: '[A-Z][_A-Z0-9]*' | 顶层属性命名 |
| `VariableNaming` | variablePattern: '[a-z][_A-Za-z0-9]*' | 变量命名（允许下划线） |

### 禁用的规则

| 规则 | 说明 |
|------|------|
| `BooleanPropertyNaming` | 布尔属性命名 |
| `ForbiddenClassName` | 禁止的类名 |
| `FunctionMaxLength` | 函数名最大长度 |
| `FunctionMinLength` | 函数名最小长度 |
| `LambdaParameterNaming` | Lambda 参数命名 |
| `NonBooleanPropertyPrefixedWithIs` | 非布尔属性使用 is 前缀 |
| `VariableMaxLength` | 变量名最大长度 |
| `VariableMinLength` | 变量名最小长度 |

---

## Performance 性能规则

### 启用的规则

| 规则 | 说明 |
|------|------|
| `ArrayPrimitive` | 使用数组而非数组包装 |
| `ForEachOnRange` | 在 Range 上使用 forEach |
| `SpreadOperator` | 使用 spread 操作符 |
| `UnnecessaryTemporaryInstantiation` | 不必要的临时对象创建 |

### 禁用的规则

| 规则 | 说明 |
|------|------|
| `CouldBeSequence` | 可以改为序列 |
| `UnnecessaryPartOfBinaryExpression` | 不必要的二进制表达式部分 |

---

## Potential Bugs 潜在 Bug 规则

### 启用的规则

| 规则 | 配置/说明 |
|------|----------|
| `AvoidReferentialEquality` | 禁止使用引用相等（String 类型） |
| `DoubleMutabilityForCollection` | 集合双重可变 |
| `EqualsAlwaysReturnsTrueOrFalse` | equals 总是返回 true 或 false |
| `EqualsWithHashCodeExist` | 重写了 equals 但没有重写 hashCode |
| `ExplicitGarbageCollectionCall` | 显式调用垃圾回收 |
| `HasPlatformType` | 平台类型 |
| `IgnoredReturnValue` | 忽略返回值 |
| `ImplicitDefaultLocale` | 隐式默认区域设置 |
| `InvalidRange` | 无效范围 |
| `IteratorHasNextCallsNextMethod` | Iterator hasNext 调用 next |
| `IteratorNotThrowingNoSuchElementException` | Iterator 不抛出 NoSuchElementException |
| `MapGetWithNotNullAssertionOperator` | Map get 使用非空断言 |
| `UnnecessaryNotNullOperator` | 不必要的不为空操作符 |
| `UnnecessarySafeCall` | 不必要的安全调用 |
| `UnreachableCatchBlock` | 不可达的 catch 块 |
| `UnreachableCode` | 不可达代码 |
| `UnsafeCallOnNullableType` | 在可空类型上不安全调用 |
| `UnsafeCast` | 不安全的强制转换 |
| `UnusedUnaryOperator` | 未使用的一元操作符 |
| `UselessPostfixExpression` | 无用的后缀表达式 |
| `WrongEqualsTypeParameter` | equals 参数类型错误 |

### 禁用的规则

| 规则 | 说明 |
|------|------|
| `CastNullableToNonNullableType` | 将可空类型强制转换 |
| `CastToNullableType` | 强制转换为可空类型 |
| `Deprecation` | 使用已废弃的 API |
| `DontDowncastCollectionTypes` | 向下转型集合类型 |
| `ElseCaseInsteadOfExhaustiveWhen` | 使用 else 而非穷举 when |
| `ExitOutsideMain` | 在 main 函数外退出 |
| `ImplicitUnitReturnType` | 隐式 Unit 返回类型 |
| `LateinitUsage` | 使用 lateinit |
| `MissingPackageDeclaration` | 缺少包声明 |
| `NullCheckOnMutableProperty` | 可变属性的空检查 |
| `NullableToStringCall` | 可空类型的 toString |
| `PropertyUsedBeforeDeclaration` | 在声明前使用属性 |
| `UnconditionalJumpStatementInLoop` | 循环中的无条件跳转 |
| `UnnecessaryNotNullCheck` | 不必要的不为空检查 |

---

## Style 代码风格规则

### 启用的规则

| 规则 | 配置 | 说明 |
|------|------|------|
| `DestructuringDeclarationWithTooManyEntries` | maxDestructuringEntries: 3 | 解构声明条目过多 |
| `EqualsNullCall` | - | equals 与 null 比较 |
| `ExplicitItLambdaParameter` | - | 显式 it lambda 参数 |
| `ForbiddenComment` | 禁止 FIXME/STOPSHIP/TODO | 禁止的注释 |
| `ForbiddenVoid` | ignoreOverridden: false | 禁止 void |
| `FunctionOnlyReturningConstant` | ignoreOverridableFunction: true | 只返回常量的函数 |
| `LoopWithTooManyJumpStatements` | maxJumpCount: 1 | 循环跳转语句过多 |
| `MayBeConst` | - | 可以用 const |
| `ModifierOrder` | - | 修饰符顺序 |
| `NestedClassesVisibility` | - | 嵌套类可见性 |
| `ObjectLiteralToLambda` | - | 对象字面量转 Lambda |
| `OptionalAbstractKeyword` | - | 可选的 abstract 关键字 |
| `ProtectedMemberInFinalClass` | - | Final 类中有 protected 成员 |
| `RedundantHigherOrderMapUsage` | - | 冗余的更高阶 map 使用 |
| `SafeCast` | - | 安全转换 |
| `SerialVersionUIDInSerializableClass` | - | Serializable 类缺少 serialVersionUID |
| `ThrowsCount` | max: 2 | 抛出计数 |
| `UnnecessaryAbstractClass` | - | 不必要的抽象类 |
| `UnnecessaryApply` | - | 不必要的 apply |
| `UnnecessaryFilter` | - | 不必要的 filter |
| `UnnecessaryInheritance` | - | 不必要的继承 |
| `UnusedPrivateClass` | - | 未使用的私有类 |
| `UseAnyOrNoneInsteadOfFind` | - | 使用 anyOrNone 而非 find |
| `UseArrayLiteralsInAnnotations` | - | 在注解中使用数组字面量 |
| `UseCheckNotNull` | - | 使用 checkNotNull |
| `UseCheckOrError` | - | 使用 check 或 error |
| `UseIsNullOrEmpty` | - | 使用 isNullOrEmpty |
| `UseOrEmpty` | - | 使用 orEmpty |
| `UseRequire` | - | 使用 require |
| `UseRequireNotNull` | - | 使用 requireNotNull |
| `UselessCallOnNotNull` | - | 对非空调用无用 |
| `UtilityClassWithPublicConstructor` | - | 工具类是公开构造函数 |
| `VarCouldBeVal` | ignoreLateinitVar: false | var 可以改为 val |

### 禁用的规则

| 规则 | 说明 |
|------|------|
| `AlsoCouldBeApply` | also 可以改为 apply |
| `BracesOnIfStatements` | if 语句大括号 |
| `BracesOnWhenStatements` | when 语句大括号 |
| `CanBeNonNullable` | 可以非空 |
| `CascadingCallWrapping` | 级联调用包装 |
| `ClassOrdering` | 类成员排序 |
| `CollapsibleIfStatements` | 可合并的 if 语句 |
| `DataClassContainsFunctions` | Data class 包含函数 |
| `DataClassShouldBeImmutable` | Data class 应该是不可变的 |
| `DoubleNegativeLambda` | 双负 lambda |
| `EqualsOnSignatureLine` | equals 在签名行 |
| `ExpressionBodySyntax` | 表达式体语法 |
| `ForbiddenAnnotation` | 禁止的注解 |
| `ForbiddenImport` | 禁止的导入 |
| `ForbiddenMethodCall` | 禁止的方法调用 |
| `ForbiddenSuppress` | 禁止的 suppress |
| `MagicNumber` | 魔数（硬编码数字） |
| `MandatoryBracesLoops` | 循环强制使用大括号 |
| `MaxChainedCallsOnSameLine` | 同一行链式调用过多 |
| `MaxLineLength` | 最大行长度 |
| `MultilineLambdaItParameter` | 多行 lambda it 参数 |
| `MultilineRawStringIndentation` | 多行原始字符串缩进 |
| `NewLineAtEndOfFile` | 文件末尾换行 |
| `NoTabs` | 不使用 Tab |
| `NullableBooleanCheck` | 可空布尔检查 |
| `OptionalUnit` | 可选的 Unit |
| `PreferToOverPairSyntax` | 使用 to 替代 Pair 语法 |
| `RedundantExplicitType` | 冗余的显式类型 |
| `RedundantVisibilityModifierRule` | 冗余的可见性修饰符 |
| `ReturnCount` | 返回计数 |
| `SpacingBetweenPackageAndImports` | 包声明和导入之间需要空行 |
| `StringShouldBeRawString` | 字符串应该是原始字符串 |
| `TrailingWhitespace` | 尾随空白 |
| `TrimMultilineRawString` | 修剪多行原始字符串 |
| `UnderscoresInNumericLiterals` | 数字字面量中的下划线 |
| `UnnecessaryAnnotationUseSiteTarget` | 不必要的注解使用目标 |
| `UnnecessaryBackticks` | 不必要的反引号 |
| `UnnecessaryBracesAroundTrailingLambda` | 尾随 lambda 周围不必要的花括号 |
| `UnnecessaryInnerClass` | 不必要的内部类 |
| `UnnecessaryLet` | 不必要的 let |
| `UnnecessaryParentheses` | 不必要的括号 |
| `UntilInsteadOfRangeTo` | 使用 until 而非 rangeTo |
| `UnusedImports` | 未使用的导入 |
| `UnusedParameter` | 未使用的参数 |
| `UnusedPrivateMember` | 未使用的私有成员 |
| `UnusedPrivateProperty` | 未使用的私有属性 |
| `UseDataClass` | 使用 data class |
| `UseEmptyCounterpart` | 使用空对应物 |
| `UseIfEmptyOrIfBlank` | 使用 ifEmpty 或 ifBlank |
| `UseIfInsteadOfWhen` | 使用 if 而非 when |
| `UseLet` | 使用 let |
| `UseSumOfInsteadOfFlatMapSize` | 使用 sumOf 而非 flatMap + size |
| `WildcardImport` | 通配符导入 |

---

## 排除目录

以下目录不参与大部分规则检查：

```
**/test/**
**/androidTest/**
**/commonTest/**
**/jvmTest/**
**/androidUnitTest/**
**/androidInstrumentedTest/**
**/jsTest/**
**/iosTest/**
**/*.kts
**/annotation/**
```

---

## 生成信息

- 配置文件: `config/detekt/detekt.yml`
- 生成时间: 2026-04-06
- Detekt 版本: 1.23.8
