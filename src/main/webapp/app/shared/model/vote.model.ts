import { IVoteTarget } from '@/shared/model/vote-target.model';
import { IVotePayout } from '@/shared/model/vote-payout.model';
import { IVoter } from '@/shared/model/voter.model';

export interface IVote {
  id?: number;
  votedTimestamp?: Date;
  verified?: boolean | null;
  verifiedTime?: Date | null;
  verifiedBy?: string | null;
  paid?: boolean;
  voteTarget?: IVoteTarget | null;
  votePayout?: IVotePayout | null;
  voter?: IVoter | null;
}

export class Vote implements IVote {
  constructor(
    public id?: number,
    public votedTimestamp?: Date,
    public verified?: boolean | null,
    public verifiedTime?: Date | null,
    public verifiedBy?: string | null,
    public paid?: boolean,
    public voteTarget?: IVoteTarget | null,
    public votePayout?: IVotePayout | null,
    public voter?: IVoter | null
  ) {
    this.verified = this.verified ?? false;
    this.paid = this.paid ?? false;
  }
}
