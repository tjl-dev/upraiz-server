import { IVoteManager } from '@/shared/model/vote-manager.model';

import { VoteCcy } from '@/shared/model/enumerations/vote-ccy.model';
export interface IVoteManagerPreferences {
  id?: number;
  payCcy?: VoteCcy | null;
  voteManager?: IVoteManager | null;
}

export class VoteManagerPreferences implements IVoteManagerPreferences {
  constructor(public id?: number, public payCcy?: VoteCcy | null, public voteManager?: IVoteManager | null) {}
}
