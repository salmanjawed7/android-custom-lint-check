package com.example.lint_checks

import com.android.tools.lint.detector.api.ConstantEvaluator
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.Scope.Companion.JAVA_FILE_SCOPE
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.skipParenthesizedExprDown

class SqrtFunctionNegativeValueDetector : Detector(), SourceCodeScanner {

  override fun getApplicableMethodNames(): List<String> = listOf("getSquareRoot")

  override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
    val evaluator = context.evaluator

    if (!evaluator.isMemberInClass(method, "com.example.androidlint.MyApi")
      || !evaluator.parameterHasType(method, 0, "double")
    ) return

    val inputValue = node.valueArguments[0].skipParenthesizedExprDown()
    val input = ConstantEvaluator.evaluate(context, inputValue).let {it as? Double}?.toDouble()
    if (input != null && input < 0) {
      // report issue here
      context.report(
        issue = ISSUE,
        scope = inputValue,
        message = "'${method.parameters[0].name}' should be greater than 0",
        location = context.getLocation(inputValue)
      )
    }
  }

  companion object {
    @JvmField val ISSUE = Issue.create(
      id = "InvalidSquareRootInput",
      briefDescription = "Invalid Input for the getSquareRoot function",
      explanation = """
        Please provide a positive value to calculate the square root  
      """,
      severity = Severity.ERROR,
      implementation = Implementation(SqrtFunctionNegativeValueDetector::class.java, JAVA_FILE_SCOPE)
    )
  }
}