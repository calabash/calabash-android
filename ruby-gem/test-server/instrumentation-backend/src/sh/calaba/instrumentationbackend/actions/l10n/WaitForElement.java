/*
 * Copyright 2012 calabash-driver committers.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package sh.calaba.instrumentationbackend.actions.l10n;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

/**
 * Action is waiting until the text of the given #l10nKey is displayed.
 * 
 * @author Dominik Dary
 * @see L10nHelper
 */
public class WaitForElement implements Action {

	@Override
	public Result execute(String... args) {
		String l10nKey = args[0];
		String pckg = (args.length > 1)? args[1] : null;
		
		String myLocalizedString = L10nHelper.getValue(l10nKey, pckg);
		boolean timedOut = !InstrumentationBackend.solo.waitForText(
				myLocalizedString, 1, 90000);
		if (timedOut) {
			return new Result(false, "Time out while waiting for text:"
					+ args[0] + ", package: " + pckg);
		} else {
			return Result.successResult();
		}
	}

	@Override
	public String key() {
		return "wait_for_l10n_element";
	}
}
