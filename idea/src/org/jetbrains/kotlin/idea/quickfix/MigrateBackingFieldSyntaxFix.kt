/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.idea.quickfix

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.diagnostics.Diagnostic
import org.jetbrains.kotlin.psi.JetFile
import org.jetbrains.kotlin.psi.JetNameReferenceExpression
import org.jetbrains.kotlin.psi.JetPsiFactory

public class MigrateBackingFieldSyntaxFix(expr: JetNameReferenceExpression)
    : KotlinQuickFixAction<JetNameReferenceExpression>(expr), CleanupFix {
    override fun invoke(project: Project, editor: Editor?, file: JetFile) {
        val replacement = JetPsiFactory(project).createExpression("field")
        element.replace(replacement)
    }

    override fun getFamilyName(): String = "Migrate backing field syntax"
    override fun getText(): String = getFamilyName()

    companion object : JetSingleIntentionActionFactory() {
        override fun createAction(diagnostic: Diagnostic): IntentionAction? {
            val element = diagnostic.psiElement as? JetNameReferenceExpression ?: return null
            return MigrateBackingFieldSyntaxFix(element)
        }
    }
}

public class MigrateBackingFieldUsageFix(expr: JetNameReferenceExpression)
    : KotlinQuickFixAction<JetNameReferenceExpression>(expr) {
    override fun invoke(project: Project, editor: Editor?, file: JetFile) {
        val replacement = JetPsiFactory(project).createExpression(element.text.substring(1))
        element.replace(replacement)
    }

    override fun getFamilyName(): String = "Replace with property access"
    override fun getText(): String = getFamilyName()

    companion object : JetSingleIntentionActionFactory() {
        override fun createAction(diagnostic: Diagnostic): IntentionAction? {
            val element = diagnostic.psiElement as? JetNameReferenceExpression ?: return null
            if (element.text.length() > 1) {
                return MigrateBackingFieldUsageFix(element)
            }
            return null
        }
    }
}
