import { IVoter } from '@/shared/model/voter.model';
import { IVotePayout } from '@/shared/model/vote-payout.model';

import { VoteCcy } from '@/shared/model/enumerations/vote-ccy.model';
export interface IVoterAccount {
  id?: number;
  accountname?: string | null;
  ccy?: VoteCcy;
  address?: string;
  voter?: IVoter | null;
  votePayouts?: IVotePayout[] | null;
}

export class VoterAccount implements IVoterAccount {
  constructor(
    public id?: number,
    public accountname?: string | null,
    public ccy?: VoteCcy,
    public address?: string,
    public voter?: IVoter | null,
    public votePayouts?: IVotePayout[] | null
  ) {}
}
