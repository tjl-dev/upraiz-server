import { IVoterAccount } from '@/shared/model/voter-account.model';
import { IVote } from '@/shared/model/vote.model';

import { VoteCcy } from '@/shared/model/enumerations/vote-ccy.model';
export interface IVotePayout {
  id?: number;
  paidTime?: Date;
  paidAmount?: number;
  paidCcy?: VoteCcy;
  voterAccount?: IVoterAccount | null;
  vote?: IVote | null;
}

export class VotePayout implements IVotePayout {
  constructor(
    public id?: number,
    public paidTime?: Date,
    public paidAmount?: number,
    public paidCcy?: VoteCcy,
    public voterAccount?: IVoterAccount | null,
    public vote?: IVote | null
  ) {}
}
