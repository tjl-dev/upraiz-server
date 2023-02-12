import { IVoter } from '@/shared/model/voter.model';

import { VoteCcy } from '@/shared/model/enumerations/vote-ccy.model';
export interface IVoterPreferences {
  id?: number;
  receiveCcy?: VoteCcy | null;
  voter?: IVoter | null;
}

export class VoterPreferences implements IVoterPreferences {
  constructor(public id?: number, public receiveCcy?: VoteCcy | null, public voter?: IVoter | null) {}
}
