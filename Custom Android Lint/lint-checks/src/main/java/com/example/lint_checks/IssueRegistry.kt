package com.example.lint_checks

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class IssueRegistry: IssueRegistry() {
  override val issues: List<Issue> = listOf(SqrtFunctionNegativeValueDetector.ISSUE)
  override val vendor: Vendor = Vendor(vendorName = "My Custom Project")
  override val api: Int = CURRENT_API
}