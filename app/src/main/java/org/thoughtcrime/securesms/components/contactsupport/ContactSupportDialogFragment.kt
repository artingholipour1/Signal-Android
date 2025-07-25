/*
 * Copyright 2025 Signal Messenger, LLC
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.thoughtcrime.securesms.components.contactsupport

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.thoughtcrime.securesms.compose.ComposeDialogFragment
import org.thoughtcrime.securesms.util.viewModel

/**
 * Three-option contact support dialog fragment.
 */
class ContactSupportDialogFragment : ComposeDialogFragment() {

  companion object {
    private const val SUBJECT = "subject"
    private const val FILTER = "filter"

    fun create(
      @StringRes subject: Int,
      @StringRes filter: Int
    ): ContactSupportDialogFragment {
      return ContactSupportDialogFragment().apply {
        arguments = bundleOf(
          SUBJECT to subject,
          FILTER to filter
        )
      }
    }
  }

  private val contactSupportViewModel: ContactSupportViewModel by viewModel {
    ContactSupportViewModel(
      showInitially = true
    )
  }

  private val subject: Int by lazy { requireArguments().getInt(SUBJECT) }
  private val filter: Int by lazy { requireArguments().getInt(FILTER) }

  @Composable
  override fun DialogContent() {
    val contactSupportState by contactSupportViewModel.state.collectAsStateWithLifecycle()

    SendSupportEmailEffect(
      contactSupportState = contactSupportState,
      subjectRes = subject,
      filterRes = filter
    ) {
      contactSupportViewModel.hideContactSupport()
      dismissAllowingStateLoss()
    }

    if (contactSupportState.show) {
      ContactSupportDialog(
        showInProgress = contactSupportState.showAsProgress,
        callbacks = contactSupportViewModel
      )
    }
  }
}
